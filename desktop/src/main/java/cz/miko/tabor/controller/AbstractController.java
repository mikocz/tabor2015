package cz.miko.tabor.controller;

import javafx.scene.Node;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public abstract class AbstractController implements Controller {

	private Node view;

	@Override
	public Node getView() {
		return view;
	}

	@Override
	public void setView(Node view) {
		this.view = view;
	}
}
