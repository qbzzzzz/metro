package plateau.ihm;

import plateau.Controleur;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

// Plateau d'UN joueur. Affichage simple et lisible :
// fond clair uni, stations, départs, et UNIQUEMENT le réseau de CE joueur (sa couleur).
// Les clics souris sont capturés ici et transmis à PanelJoueur.
public class PanelGrille extends JPanel
{
	private Controleur  ctrl;
	private PanelJoueur panelJoueur;
	private int         numeroJoueur;

	public PanelGrille(Controleur ctrl, PanelJoueur panelJoueur, int numeroJoueur)
	{
		this.ctrl         = ctrl;
		this.panelJoueur  = panelJoueur;
		this.numeroJoueur = numeroJoueur;

		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int largeur = ctrl.getLargeur();
				int hauteur = ctrl.getHauteur();
				if (largeur <= 0 || hauteur <= 0) return;

				int cellW = getWidth()  / largeur;
				int cellH = getHeight() / hauteur;
				if (cellW <= 0 || cellH <= 0) return;

				int col = e.getX() / cellW;
				int row = e.getY() / cellH;
				if (col >= largeur) col = largeur - 1;
				if (row >= hauteur) row = hauteur - 1;

				panelJoueur.caseCliquee(row * largeur + col);
			}
		});
	}

	// (Re)dimensionne le panneau selon le plateau chargé
	public void construireGrille()
	{
		int largeur    = this.ctrl.getLargeur();
		int hauteur    = this.ctrl.getHauteur();
		int cellTaille = 60;
		this.setPreferredSize(new Dimension(largeur * cellTaille, hauteur * cellTaille));
		this.revalidate();
		this.repaint();
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int w       = getWidth();
		int h       = getHeight();
		int largeur = this.ctrl.getLargeur();
		int hauteur = this.ctrl.getHauteur();
		if (largeur <= 0 || hauteur <= 0) return;

		int cellW  = w / largeur;
		int cellH  = h / hauteur;
		int taille = largeur * hauteur;

		// --- 1. Fond clair uni ---
		g.setColor(new Color(245, 245, 245));
		g.fillRect(0, 0, w, h);

		// --- 2. Zones d'arrondissement en arrière-plan ---
		Color[] tabCouleurs = plateau.metier.UtilitaireJeu.getCouleurs();
		for (int i = 0; i < taille; i++)
		{
			int arr = this.ctrl.getArrondissement(i);
			if (arr >= 1 && arr <= tabCouleurs.length)
			{
				int x = (i % largeur) * cellW;
				int y = (i / largeur) * cellH;
				Color c = tabCouleurs[arr - 1];
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 110));
				g.fillRect(x, y, cellW, cellH);
			}
		}

		Color couleur = this.ctrl.getCouleurJoueur(this.numeroJoueur);

		// --- 3. Ligne du réseau de CE joueur (dans l'ordre de pose) ---
		// On ne colore PAS le fond des cases du réseau : seul le tracé matérialise la ligne,
		// l'arrière-plan (arrondissement) reste inchangé quand on pose une station.
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(couleur);

		ArrayList<Integer> chemin = this.ctrl.getCheminJoueur(this.numeroJoueur);
		for (int idx = 0; idx < chemin.size() - 1; idx++)
		{
			int s1 = chemin.get(idx);
			int s2 = chemin.get(idx + 1);
			int x1 = (s1 % largeur) * cellW + cellW / 2;
			int y1 = (s1 / largeur) * cellH + cellH / 2;
			int x2 = (s2 % largeur) * cellW + cellW / 2;
			int y2 = (s2 / largeur) * cellH + cellH / 2;
			g2d.drawLine(x1, y1, x2, y2);
		}

		// --- 4. Stations, départs (texte noir) et surbrillance des coups valides ---
		ArrayList<Integer> valides = this.ctrl.getCasesValides(this.numeroJoueur);
		int fontSize = Math.max(9, Math.min(cellH / 3, 14));

		for (int i = 0; i < taille; i++)
		{
			int x = (i % largeur) * cellW;
			int y = (i / largeur) * cellH;

			int station = this.ctrl.getStation(i);
			if (station > 0)
			{
				Image img = this.ctrl.getImageStation(station);
				if (img != null)
					g.drawImage(img, x + 3, y + 3, cellW - 6, cellH - 6, this);
			}

			int depart = this.ctrl.getDepart(i);
			if (depart > 0)
			{
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.BOLD, fontSize));
				g.drawString("D" + depart, x + 3, y + fontSize + 2);
			}

			if (valides.contains(i))
			{
				g.setColor(new Color(255, 255, 0, 120));
				g.fillRect(x, y, cellW, cellH);
			}
		}
	}
}
