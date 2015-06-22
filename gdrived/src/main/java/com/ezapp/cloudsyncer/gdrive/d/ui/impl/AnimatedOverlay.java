package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.SystemColor;

/**
 * Panel to display animated overlay
 * 
 * @author grangarajan
 *
 */
class AnimatedOverlay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8534708055673015990L;

	/**
	 * 
	 */
	public AnimatedOverlay() {
		setBackground(UIManager.getColor("Button.background"));
		initUI();
	}

	/**
	 * @param layout
	 */
	public AnimatedOverlay(LayoutManager layout) {
		super(layout);
		initUI();
	}

	/**
	 * @param isDoubleBuffered
	 */
	public AnimatedOverlay(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		initUI();
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public AnimatedOverlay(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		initUI();
	}

	public void initUI(){
		setSize(197, 94);
		setLayout(null);
		
		JLabel lblAnimator = new JLabel("");
		lblAnimator.setBackground(SystemColor.text);
		lblAnimator.setIcon(new ImageIcon(AnimatedOverlay.class.getResource("/com/ezapp/cloudsyncer/gdrive/d/images/loader.gif")));
		lblAnimator.setBounds(10, 11, 177, 72);
		add(lblAnimator);
	}
	
}
