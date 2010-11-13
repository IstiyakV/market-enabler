package ch.racic.android.marketenabler;

import android.util.Log;

import com.androidiani.MarketEnabler.presenter.ShellInterface;

public class Executor implements Runnable{
	private static String[] writePropCommand;
	private static String code;
	
	public Executor(String codeToFake) {
		super();
		code = codeToFake;
		setValues();
		
	}

	private void setValues() {
		Log.d("MarketEnabler", "auto setValues with code " + code + " gets executed");
		String[] writePropCommand = {
				"setprop gsm.sim.operator.numeric " + code,
				"killall com.android.vending",
				"rm -rf /data/data/com.android.vending/cache/*" };

		setCommand(writePropCommand);
		Thread thread = new Thread(this);
		thread.start();
	}
	
	private static void setCommand(String[] cmd) {
		writePropCommand = cmd;
	}
	
	@Override
	public void run() {
		Log.d("MarketEnabler", "Starting shell thread with ["
				+ writePropCommand.length + "] commands");
		ShellInterface.doExec(writePropCommand, true, null);
	}
}
