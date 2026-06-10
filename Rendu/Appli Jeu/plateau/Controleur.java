package plateau;

import plateau.ihm.FrameAccueil;
import plateau.ihm.FrameInfos;
import plateau.ihm.FrameJoueur;
import plateau.metier.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Controleur
{
	private FrameAccueil  ihm;
	private Plateau       metier;

	// Configuration de la partie
	private int           nbJoueurs;
	private int           nbStations;
	private String        mode;        // "LOCAL" ou "MULTIJOUEUR"

	// Partie en cours (mode local)
	private Partie        partie;
	private FrameJoueur[] framesJoueurs;
	private FrameInfos    frameInfos;     // bande verticale centrale
	private boolean       resultatsAffiches;

	// Couleurs des 4 joueurs (partagées entre IHM et contrôleur)
	public static final Color[] COULEURS_JOUEURS = {
		new Color(220,  50,  50),  // Joueur 1 rouge
		new Color( 50, 100, 220),  // Joueur 2 bleu
		new Color( 50, 180,  50),  // Joueur 3 vert
		new Color(220, 140,   0)   // Joueur 4 orange
	};

	public Controleur()
	{
		this.metier = new Plateau(10, 10);
		this.ihm    = new FrameAccueil ( this );
	}

	/*-------------------------------------*/
	/* Configuration de la partie          */
	/*-------------------------------------*/
	public void initialiserPlateau(int largeur, int hauteur)
	{
		this.metier = new Plateau(largeur, hauteur);
	}

	public void setConfigJeu(int nbJoueurs, int nbStations)
	{
		this.nbJoueurs  = nbJoueurs;
		this.nbStations = nbStations;
	}

	public int getNbJoueurs()  { return this.partie != null ? this.partie.getNbJoueurs()  : this.nbJoueurs;  }

	public int getNbStations() { return this.partie != null ? this.partie.getNbStations() : this.nbStations; }

	public void   setMode(String mode) { this.mode = mode; }

	public String getMode()            { return this.mode; }

	/*-------------------------------------*/
	/* Chargement d'un plateau             */
	/*-------------------------------------*/
	public boolean chargerPlateau(java.io.File fichier)
	{
		Plateau p = ChargeurPlateau.charger(fichier);
		if (p != null)
		{
			this.metier = p;
			return true;
		}
		return false;
	}

	public void mettreAJourConfigurationDepuisPlateau()
	{
		int[] maxConfig = ConfigurationPlateau.detecterMaxJoueursEtStations(this.metier, this.nbJoueurs, this.nbStations);
		this.setConfigJeu(maxConfig[0], maxConfig[1]);
	}

	public boolean validerDepartsPlateau()
	{
		return ValidateurPlateau.validerDeparts(this.metier, this.nbJoueurs);
	}

	public boolean enregistrerPlateau(String nomFichier)
	{
		return EnregistreurPlateau.enregistrer(this.metier, nomFichier);
	}

	// Renvoie la liste des plateaux (.txt) conçus dans l'Appli Conception.
	public java.io.File[] getSauvegardes()
	{
		String[] chemins = {
			"../Appli Conception/plateau/sauvegarde",
			"../../Appli Conception/plateau/sauvegarde",
			"plateau/sauvegarde",
			"sauvegarde"
		};

		for (String chemin : chemins)
		{
			java.io.File dir = new java.io.File(chemin);
			if (dir.exists() && dir.isDirectory())
			{
				java.io.File[] liste = dir.listFiles();
				if (liste != null)
				{
					java.util.ArrayList<java.io.File> txt = new java.util.ArrayList<java.io.File>();
					for (java.io.File f : liste)
					{
						if (f.isFile() && f.getName().endsWith(".txt"))
						{
							txt.add(f);
						}
					}
					if (!txt.isEmpty()) return txt.toArray(new java.io.File[0]);
				}
			}
		}

		return new java.io.File[0];
	}

	// Vérifie que le plateau chargé est jouable (il possède au moins un départ et une station)
	public boolean plateauEstJouable()
	{
		this.setConfigJeu(0, 0);
		this.mettreAJourConfigurationDepuisPlateau();
		return this.nbJoueurs > 0 && this.nbStations > 0;
	}

	/*-------------------------------------*/
	/* Partie en LOCAL                     */
	/*-------------------------------------*/
	// Démarre la partie locale : crée la partie (pioche commune), la bande d'infos centrale
	// puis une fenêtre (plateau seul) par joueur, et place le tout à l'écran.
	public void lancerPartieLocale()
	{
		this.partie            = new Partie(this.metier);
		this.resultatsAffiches = false;

		// Bande verticale centrale (carte commune + infos)
		this.frameInfos = new FrameInfos(this);

		// Une fenêtre par joueur (uniquement le plateau)
		int nb = this.partie.getNbJoueurs();
		this.framesJoueurs = new FrameJoueur[nb];
		for (int i = 0; i < nb; i++)
		{
			this.framesJoueurs[i] = new FrameJoueur(this, i + 1);
		}

		placerFenetres();
	}

	// Place la bande d'infos au centre et les plateaux de part et d'autre (gauche / droite)
	private void placerFenetres()
	{
		Dimension ecran = Toolkit.getDefaultToolkit().getScreenSize();

		int bandeW = this.frameInfos.getWidth();
		int bandeH = Math.min(this.frameInfos.getHeight(), ecran.height - 60);
		int bx     = (ecran.width - bandeW) / 2;
		this.frameInfos.setBounds(bx, 20, bandeW, bandeH);

		int nb     = this.framesJoueurs.length;
		int gap    = 15;
		int rangs  = (nb <= 2) ? 1 : 2;
		int boardW = Math.min(460, bx - gap - 10);
		int boardH = Math.min(520, (ecran.height - 60) / rangs - 10);

		for (int i = 0; i < nb; i++)
		{
			boolean gauche = (i % 2 == 0);            // pairs à gauche, impairs à droite
			int rang = i / 2;
			int x = gauche ? (bx - gap - boardW) : (bx + bandeW + gap);
			int y = 20 + rang * (boardH + 15);
			if (x < 0) x = 0;
			this.framesJoueurs[i].setBounds(x, y, boardW, boardH);
		}
	}

	// Un joueur pose la carte commune sur une case. Retourne true si le coup est valide.
	public boolean jouerCoup(int numeroJoueur, int numCase)
	{
		return this.partie != null && this.partie.jouerCoup(numeroJoueur, numCase);
	}

	// Un joueur passe son tour pour la carte commune
	public void passerTour(int numeroJoueur)
	{
		if (this.partie != null) this.partie.passerTour(numeroJoueur);
	}

	// Rafraîchit TOUTES les fenêtres joueurs (les frames sont liées entre elles)
	public void rafraichirTout()
	{
		if (this.frameInfos != null) this.frameInfos.rafraichir();

		if (this.framesJoueurs != null)
		{
			for (int i = 0; i < this.framesJoueurs.length; i++)
			{
				if (this.framesJoueurs[i] != null) this.framesJoueurs[i].rafraichir();
			}
		}

		// Fin de partie (version simple) : message unique quand la pioche est épuisée
		if (this.partie != null && this.partie.isPartieTerminee() && !this.resultatsAffiches)
		{
			this.resultatsAffiches = true;
			JOptionPane.showMessageDialog(null, "Manche terminée ! Toutes les cartes foncées ont été tirées.");
		}
	}

	// --- Accès à la partie (pour l'IHM de jeu) ---
	public boolean            isPartieTerminee()                       { return this.partie != null && this.partie.isPartieTerminee(); }
	public boolean            aJoue(int numeroJoueur)                  { return this.partie != null && this.partie.aJoue(numeroJoueur); }
	public boolean            estSonTour(int numeroJoueur)             { return this.partie != null && this.partie.estSonTour(numeroJoueur); }
	public int                getJoueurCourant()                       { return this.partie == null ? 0 : this.partie.getJoueurCourant(); }
	public Carte              getCarteCourante()                       { return this.partie == null ? null : this.partie.getCarteCourante(); }
	public int                getNbCartesRestantes()                   { return this.partie == null ? 0    : this.partie.getNbCartesRestantes(); }
	public int                getNbFonceesRestantes()                  { return this.partie == null ? 0    : this.partie.getNbFonceesRestantes(); }
	public boolean            estDansReseau(int numeroJoueur, int i)   { return this.partie != null && this.partie.estDansReseau(numeroJoueur, i); }
	public ArrayList<Integer> getCasesValides(int numeroJoueur)        { return this.partie == null ? new ArrayList<Integer>() : this.partie.getCasesValides(numeroJoueur); }
	public ArrayList<Integer> getCheminJoueur(int numeroJoueur)        { return this.partie == null ? new ArrayList<Integer>() : this.partie.getCheminJoueur(numeroJoueur); }

	public Color getCouleurJoueur(int numeroJoueur)
	{
		if (numeroJoueur >= 1 && numeroJoueur <= COULEURS_JOUEURS.length)
			return COULEURS_JOUEURS[numeroJoueur - 1];
		return Color.WHITE;
	}

	/*-------------------------------------*/
	/* Accès au plateau (métier)           */
	/*-------------------------------------*/
	public int getLargeur() { return this.metier.getLargeur(); }

	public int getHauteur() { return this.metier.getHauteur(); }

	public int getArrondissement(int numCase)
	{
		return this.metier.getArrondissement(numCase);
	}

	public void affecterArrondissement(int numCase, int arrondissement)
	{
		this.metier.affecterArrondissement( numCase, arrondissement );
	}

	public void affecterStation(int numCase, int station)
	{
		this.metier.affecterStation(numCase, station);
	}

	public int getStation(int numCase)
	{
		return this.metier.getStation(numCase);
	}

	public void affecterDepart(int numCase, int depart)
	{
		this.metier.affecterDepart(numCase, depart);
	}

	public int getDepart(int numCase)
	{
		return this.metier.getDepart(numCase);
	}

	public boolean aArete(int i, int j)
	{
		return this.metier.getGraphe().aArete(i, j);
	}

	public int getNbAretes()
	{
		return this.metier.getGraphe().getNbAretes();
	}

	public void genererAretesAuto()
	{
		this.metier.getGraphe().genererAretesAuto(this.metier);
	}

	/*-------------------------------------*/
	/* Images des stations (cache)         */
	/*-------------------------------------*/
	private Image[] stationImages = new Image[11];

	public Image getImageStation(int stationNum)
	{
		if (stationNum < 1 || stationNum >= this.stationImages.length) return null;
		if (this.stationImages[stationNum] == null)
		{
			String chemin = UtilitaireJeu.getCheminImageStation(stationNum);
			if (chemin != null)
				this.stationImages[stationNum] = new ImageIcon(chemin).getImage();
		}
		return this.stationImages[stationNum];
	}

	/*-------------------------------------*/
	/* Images de fond                      */
	/*-------------------------------------*/
	public String getImageFond()
	{
		String[] chemins = {
			"plateau/images/fond.png",
			"images/fond.png",
			"../plateau/images/fond.png",
			"../../plateau/images/fond.png"
		};

		for (String chemin : chemins)
		{
			java.io.File fichier = new java.io.File(chemin);
			if (fichier.exists())
			{
				return fichier.getAbsolutePath();
			}
		}

		return "plateau/images/fond.png";
	}

	public String getImageFond2()
	{
		String[] chemins = {
			"plateau/images/fond2.png",
			"images/fond2.png",
			"../plateau/images/fond2.png",
			"../../plateau/images/fond2.png"
		};

		for (String chemin : chemins)
		{
			java.io.File fichier = new java.io.File(chemin);
			if (fichier.exists())
			{
				return fichier.getAbsolutePath();
			}
		}

		return "plateau/images/fond2.png";
	}

	public static void main(String[] a)
	{
		new Controleur();
	}
}
