package com.github.ants280.compGeo.ui.worker;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public abstract class CompGeoPopupWorker<T>
{
	private static final int INITIAL_POPUP_DELAY = 1_000; // in milliseconds
	private static final String SHOW_POPUP_COMMAND = "showPopup";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";
	private final CompGeoSwingWorker<T> swingWorker;
	private final JDialog popupDialog;
	private final Timer delayTimer;

	public CompGeoPopupWorker(Consumer<T> completedActionConsumer, Frame popupOwner, String popupDialogTitle)
	{
		this.swingWorker = new CompGeoSwingWorker<>(this::doInBackground, completedActionConsumer);
		this.popupDialog = new JDialog(popupOwner, popupDialogTitle, true);
		this.delayTimer = this.initDelayTimer();
	}

	protected abstract T doInBackground();

	protected final void updateProgress(double additionalProgress)
	{
		swingWorker.incrementProgress(additionalProgress);
	}

	public void start()
	{
		swingWorker.execute();

		delayTimer.start();
	}

	private Timer initDelayTimer()
	{
		Timer timer = new Timer(-1, this::showPopup); // -1 = only fire once
		timer.setInitialDelay(INITIAL_POPUP_DELAY);
		timer.setActionCommand(SHOW_POPUP_COMMAND);
		timer.setRepeats(false);

		return timer;
	}

	private void showPopup(ActionEvent event)
	{
		if (swingWorker.isDone() || !SHOW_POPUP_COMMAND.equals(event.getActionCommand()))
		{
			return;
		}

		synchronized (popupDialog)
		{
			this.initPopupDialog();
			popupDialog.setVisible(true);
		}
	}

	private void initPopupDialog()
	{
		JButton cancelButton = new JButton(CANCEL_BUTTON_TEXT);
		cancelButton.addActionListener(this::handleActionEvent);

		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		swingWorker.addPropertyChangeListener(propertyChangeEvent -> setProgress(progressBar, propertyChangeEvent));

		JPanel panel = new JPanel();
		panel.add(progressBar, BorderLayout.CENTER);
		panel.add(cancelButton, BorderLayout.PAGE_END);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		popupDialog.add(panel);
		popupDialog.pack();
		popupDialog.setResizable(false);
		popupDialog.setLocationRelativeTo(popupDialog.getParent());
		popupDialog.addWindowListener(new CompGeoPopupWindowListener<>(this));
	}

	private void handleActionEvent(ActionEvent event)
	{
		if (CANCEL_BUTTON_TEXT.equals(event.getActionCommand()))
		{
			this.cancel();
		}
	}

	private void cancel()
	{
		swingWorker.cancel(true);

		popupDialog.setVisible(false);
	}

	private static void setProgress(JProgressBar progressBar, PropertyChangeEvent propertyChangeEvent)
	{
		if ("progress".equals(propertyChangeEvent.getPropertyName())) // Copied from SwingWorker:201
		{
			progressBar.setValue((int) propertyChangeEvent.getNewValue());
		}
	}

	private class CompGeoSwingWorker<U> extends SwingWorker<U, Void>
	{
		private final Supplier<U> backgroundSupplier;
		private final Consumer<U> completedActionConsumer;
		private double progress;

		public CompGeoSwingWorker(Supplier<U> backgroundSupplier, Consumer<U> completedActionConsumer)
		{
			this.backgroundSupplier = backgroundSupplier;
			this.completedActionConsumer = completedActionConsumer;
			this.progress = 0;
		}

		@Override
		protected U doInBackground()
		{
			return backgroundSupplier.get();
		}

		@Override
		protected void done()
		{
			if (!this.isCancelled())
			{
				try
				{
					completedActionConsumer.accept(this.get());
				}
				catch (InterruptedException | ExecutionException ex)
				{
					throw new RuntimeException("Problem completing action", ex);
				}
			}

			synchronized (popupDialog)
			{
				if (popupDialog.isVisible())
				{
					popupDialog.setVisible(false);
				}
			}
		}

		protected void incrementProgress(double additionalProgress)
		{
			progress += additionalProgress;

			this.setProgress((int) (100 * progress));
		}
	}

	private static class CompGeoPopupWindowListener<T> extends WindowAdapter
	{
		private final CompGeoPopupWorker<T> popupWorker;

		public CompGeoPopupWindowListener(CompGeoPopupWorker<T> popup)
		{
			this.popupWorker = popup;
		}

		@Override
		public void windowClosing(WindowEvent event)
		{
			popupWorker.cancel();
		}
	}
}
