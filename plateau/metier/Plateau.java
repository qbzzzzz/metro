package plateau.metier;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Scanner;

public class Plateau
{
	private int[] tabCases;
	private int[] tabMetros;
	private int[] tabDeparts;
	private int   largeur;
	private int   hauteur;

	public Plateau(int largeur, int hauteur)
	{
		this.largeur  = largeur;
		this.hauteur  = hauteur;
		this.tabCases = new int[largeur * hauteur];
		this.tabMetros = new int[largeur * hauteur];
		this.tabDeparts = new int[largeur * hauteur];
	}

	public int getLargeur() { return this.largeur; }
	public int getHauteur() { return this.hauteur; }

	public int getArrondissement(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabCases.length)
			return this.tabCases[numCase];
		return 0;
	}

	public void affecterArrondissement(int numCase, int arrondissement)
	{
		if (numCase >= 0 && numCase < this.tabCases.length)
			this.tabCases[numCase] = arrondissement;
	}

	public void affecterMetro(int numCase, int metro)
	{
		if (numCase >= 0 && numCase < this.tabMetros.length)
			this.tabMetros[numCase] = metro;
	}

	public int getMetro(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabMetros.length)
			return this.tabMetros[numCase];
		return 0;
	}

	public void affecterDepart(int numCase, int depart)
	{
		if (numCase >= 0 && numCase < this.tabDeparts.length)
			this.tabDeparts[numCase] = depart;
	}

	public int getDepart(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabDeparts.length)
			return this.tabDeparts[numCase];
		return 0;
	}

	public boolean chargerPlateau(File file)
	{
		try (Scanner scanner = new Scanner(file))
		{
			if (!scanner.hasNextLine()) return false;
			String firstLine = scanner.nextLine();
			String[] dim = firstLine.split(";");
			this.largeur = Integer.parseInt(dim[0]);
			this.hauteur = Integer.parseInt(dim[1]);
			
			int size = this.largeur * this.hauteur;
			this.tabCases = new int[size];
			this.tabMetros = new int[size];
			this.tabDeparts = new int[size];

			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (line.trim().isEmpty()) continue;
				String[] parts = line.split(";");
				int index = Integer.parseInt(parts[0]);
				if (index >= 0 && index < size)
				{
					this.tabCases[index] = Integer.parseInt(parts[1]);
					
					// Compatibilité avec l'ancien format (2 colonnes) ou le nouveau format (4 colonnes)
					if (parts.length >= 3)
					{
						this.tabMetros[index] = Integer.parseInt(parts[2]);
					}
					if (parts.length >= 4)
					{
						this.tabDeparts[index] = Integer.parseInt(parts[3]);
					}
				}
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean enregistrerPlateau(String nomFichier)
	{
		try
		{
			java.io.File dossier = new java.io.File("sauvegarde");
			if (!dossier.exists())
			{
				dossier.mkdirs();
			}

			File file = new java.io.File(dossier, nomFichier.endsWith(".txt") ? nomFichier : nomFichier + ".txt");
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));

			// Écrit la largeur et la hauteur en premier
			pw.println(this.largeur + ";" + this.hauteur);

			for (int i = 0; i < this.tabCases.length; i++)
			{
				pw.println(i + ";" + this.tabCases[i] + ";" + this.tabMetros[i] + ";" + this.tabDeparts[i]);
			}

			pw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		System.out.println("Enregistrement du plateau en cours...");
		System.out.println("Plateau validé !");
		return true;
	}
}