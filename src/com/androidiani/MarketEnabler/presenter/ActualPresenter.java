package com.androidiani.MarketEnabler.presenter;

import java.util.List;

import android.util.Log;


public class ActualPresenter {
	private IActualView view;
	List<String> shellRes;

	String[] readPropCommand = { "getprop gsm.sim.operator.numeric",
			"getprop gsm.operator.numeric",
			"getprop gsm.sim.operator.iso-country",
			"getprop gsm.operator.iso-country", "getprop gsm.operator.alpha",
			"getprop gsm.sim.operator.alpha" };

	public ActualPresenter(IActualView viewIn) {
		view = viewIn;
		updateView();
	}

	public void updateView() {
		Log.i("MarketEnabler", "dropping shell commands to update actual view");
		shellRes = ShellInterface.doExec(readPropCommand, false);
		if (shellRes.size() != 6) {
			String errorMsg = "";
			for (String shellResElem : shellRes) {
				errorMsg += shellResElem + "\n";
			}
		} else {
			view.setSimNumeric(shellRes.get(0));
			view.setOperatorNumeric(shellRes.get(1));
			view.setSimISO(shellRes.get(2));
			view.setOperatorISO(shellRes.get(3));
			view.setOperatorAlpha(shellRes.get(4));
			view.setSimAlpha(shellRes.get(5));
			shellRes = null;
		}
	}
}
