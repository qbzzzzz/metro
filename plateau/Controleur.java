package plateau;

import plateau.ihm.FrameAccueil;
import plateau.metier.Plateau;

public class Controleur
{
	private FrameAccueil ihm;
	private Plateau      metier;

	public Controleur()
	{
		this.metier = new Plateau(10, 10);
		this.ihm    = new FrameAccueil ( this );
	}

	private int nbJoueurs;
	private int nbMetro;

	public void initialiserPlateau(int largeur, int hauteur)
	{
		this.metier = new Plateau(largeur, hauteur);
	}

	public void setConfigJeu(int nbJoueurs, int nbMetro)
	{
		this.nbJoueurs = nbJoueurs;
		this.nbMetro = nbMetro;
	}

	public int getNbJoueurs()
	{
		return this.nbJoueurs;
	}

	public int getNbMetro()
	{
		return this.nbMetro;
	}

	public boolean chargerPlateau(java.io.File file)
	{
		return this.metier.chargerPlateau(file);
	}

	public int getLargeur()
	{
		return this.metier.getLargeur();
	}

	public int getHauteur()
	{
		return this.metier.getHauteur();
	}

	public int getArrondissement(int numCase)
	{
		return this.metier.getArrondissement(numCase);
	}

	public void affecterMetro(int numCase, int metro)
	{
		this.metier.affecterMetro(numCase, metro);
	}

	public int getMetro(int numCase)
	{
		return this.metier.getMetro(numCase);
	}

	public void affecterDepart(int numCase, int depart)
	{
		this.metier.affecterDepart(numCase, depart);
	}

	public int getDepart(int numCase)
	{
		return this.metier.getDepart(numCase);
	}

	public boolean enregistrerPlateau(String nomFichier)
	{
		return this.metier.enregistrerPlateau(nomFichier);
	}

	public void affecterArrondissement(int numCase, int arrondissement)
	{
		this.metier.affecterArrondissement( numCase, arrondissement );
	}

	public String getImageFond()
	{
		String[] paths = {
			"plateau/images/fond.png",
			"images/fond.png",
			"../plateau/images/fond.png",
			"../../plateau/images/fond.png"
		};

		for (String path : paths)
		{
			java.io.File file = new java.io.File(path);
			if (file.exists())
			{
				return file.getAbsolutePath();
			}
		}

		return "plateau/images/fond.png";
	}

	public String getImageFond2()
	{
		String[] paths = {
			"plateau/images/fond2.png",
			"images/fond2.png",
			"../plateau/images/fond2.png",
			"../../plateau/images/fond2.png"
		};

		for (String path : paths)
		{
			java.io.File file = new java.io.File(path);
			if (file.exists())
			{
				return file.getAbsolutePath();
			}
		}

		return "plateau/images/fond2.png";
	}

	public static void main(String[] a)
	{
		new Controleur();
	}
}
