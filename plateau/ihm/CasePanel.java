package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import plateau.Controleur;
import plateau.metier.UtilitaireJeu;

public class CasePanel extends JPanel
{
	private int index;
	private Controleur ctrl;
	private PanelJeu panelJeu;
	private Color[] tabCouleurs;

	public CasePanel(int index, Controleur ctrl, PanelJeu panelJeu)
	{
		this.index = index;
		this.ctrl = ctrl;
		this.panelJeu = panelJeu;
		this.tabCouleurs = UtilitaireJeu.getCouleurs();

		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setPreferredSize(new Dimension(50, 50));
		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				CasePanel.this.panelJeu.caseCliquee(CasePanel.this.index);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int w = getWidth();
		int h = getHeight();

		// 1. Dessiner le fond (arrondissement)
		int arr = ctrl.getArrondissement(index);
		if (arr == 0)
		{
			g.setColor(Color.LIGHT_GRAY);
		}
		else if (arr > 0 && arr <= tabCouleurs.length)
		{
			g.setColor(tabCouleurs[arr - 1]);
		}
		else
		{
			g.setColor(Color.WHITE);
		}
		g.fillRect(0, 0, w, h);

		// 2. Dessiner l'image de la Station
		int station = ctrl.getStation(index);
		if (station > 0)
		{
			Image img = panelJeu.getImageStation(station);
			if (img != null)
			{
				g.drawImage(img, 2, 2, w - 4, h - 4, this);
			}
		}

		// 3. Dessiner le départ du joueur
		int depart = ctrl.getDepart(index);
		if (depart > 0)
		{
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.drawString("D" + depart, 5, 18);
		}
	}
}
