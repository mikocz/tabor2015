package cz.miko.tabor.controller;

import javafx.scene.Node;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public interface Controller {
	Node getView();
	void setView (Node view);
}
