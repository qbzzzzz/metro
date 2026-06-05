package plateau.metier;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Scanner;

public class Plateau
{
	private int[] tabCases;
	private int[] tabStations;
	private int[] tabDeparts;
	private int   largeur;
	private int   hauteur;
	private boolean[][] matriceAretes;

	public Plateau(int largeur, int hauteur)
	{
		this.largeur  = largeur;
		this.hauteur  = hauteur;
		this.tabCases = new int[largeur * hauteur];
		this.tabStations = new int[largeur * hauteur];
		this.tabDeparts = new int[largeur * hauteur];
		this.matriceAretes = new boolean[largeur * hauteur][largeur * hauteur];
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

	public void affecterStation(int numCase, int station)
	{
		if (numCase >= 0 && numCase < this.tabStations.length)
			this.tabStations[numCase] = station;
	}

	public int getStation(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabStations.length)
			return this.tabStations[numCase];
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

	public boolean aArete(int i, int j)
	{
		int size = this.largeur * this.hauteur;
		if (i >= 0 && i < size && j >= 0 && j < size)
		{
			return this.matriceAretes[i][j];
		}
		return false;
	}

	public void genererAretesAuto()
	{
		int size = this.largeur * this.hauteur;
		this.matriceAretes = new boolean[size][size];

		// Les 8 directions : N, NE, E, SE, S, SW, W, NW
		int[] dx = { 0,  1, 1, 1, 0, -1, -1, -1};
		int[] dy = {-1, -1, 0, 1, 1,  1,  0, -1};

		for (int i = 0; i < size; i++)
		{
			if (this.tabStations[i] == 0) continue;

			int x = i % this.largeur;
			int y = i / this.largeur;

			for (int d = 0; d < 8; d++)
			{
				int cx = x + dx[d];
				int cy = y + dy[d];

				while (cx >= 0 && cx < this.largeur && cy >= 0 && cy < this.hauteur)
				{
					int targetIndex = cy * this.largeur + cx;
					if (this.tabStations[targetIndex] > 0)
					{
						this.matriceAretes[i][targetIndex] = true;
						this.matriceAretes[targetIndex][i] = true;
						break; // S'arrêter au sommet le plus proche dans cette direction
					}
					cx += dx[d];
					cy += dy[d];
				}
			}
		}
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
			this.tabStations = new int[size];
			this.tabDeparts = new int[size];
			this.matriceAretes = new boolean[size][size];

			int cellCount = 0;
			boolean hasEdgesInFile = false;

			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (line.trim().isEmpty()) continue;
				String[] parts = line.split(";");
				
				if (cellCount < size)
				{
					int index = Integer.parseInt(parts[0]);
					if (index >= 0 && index < size)
					{
						this.tabCases[index] = Integer.parseInt(parts[1]);
						if (parts.length >= 3)
						{
							this.tabStations[index] = Integer.parseInt(parts[2]);
						}
						if (parts.length >= 4)
						{
							this.tabDeparts[index] = Integer.parseInt(parts[3]);
						}
					}
					cellCount++;
				}
				else
				{
					// Ce sont des arêtes : format src;dest
					int src = Integer.parseInt(parts[0]);
					int dest = Integer.parseInt(parts[1]);
					if (src >= 0 && src < size && dest >= 0 && dest < size)
					{
						this.matriceAretes[src][dest] = true;
						this.matriceAretes[dest][src] = true;
					}
					hasEdgesInFile = true;
				}
			}

			// Si le fichier n'avait pas d'arêtes pré-enregistrées (ex: plateau_prof.txt)
			if (!hasEdgesInFile)
			{
				genererAretesAuto();
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
			// Regénérer les arêtes automatiquement avant d'enregistrer
			genererAretesAuto();

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
				pw.println(i + ";" + this.tabCases[i] + ";" + this.tabStations[i] + ";" + this.tabDeparts[i]);
			}

			// Écrit les arêtes (uniquement une fois pour chaque paire)
			int size = this.largeur * this.hauteur;
			for (int i = 0; i < size; i++)
			{
				for (int j = i + 1; j < size; j++)
				{
					if (this.matriceAretes[i][j])
					{
						pw.println(i + ";" + j);
					}
				}
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