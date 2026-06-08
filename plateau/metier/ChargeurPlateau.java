package plateau.metier;

import java.io.File;
import java.util.Scanner;

public class ChargeurPlateau
{
	public static Plateau charger(File fichier)
	{
		try (Scanner scanner = new Scanner(fichier))
		{
			if (!scanner.hasNextLine()) return null;
			
			String[] dimensions = scanner.nextLine().split(";");
			int largeur = Integer.parseInt(dimensions[0]);
			int hauteur = Integer.parseInt(dimensions[1]);
			
			Plateau plateau = new Plateau(largeur, hauteur);
			int taille = largeur * hauteur;

			int compteurCases = 0;
			while (scanner.hasNextLine())
			{
				String ligne = scanner.nextLine().trim();
				if (ligne.isEmpty()) continue;
				String[] elements = ligne.split(";");
				
				if (compteurCases < taille)
				{
					int index = Integer.parseInt(elements[0]);
					plateau.affecterArrondissement(index, Integer.parseInt(elements[1]));
					if (elements.length >= 3) plateau.affecterStation(index, Integer.parseInt(elements[2]));
					if (elements.length >= 4) plateau.affecterDepart(index, Integer.parseInt(elements[3]));
					compteurCases++;
				}
				else
				{
					int source = Integer.parseInt(elements[0]);
					int destination = Integer.parseInt(elements[1]);
					plateau.getGraphe().ajouterArete(source, destination);
				}
			}

			// Si le fichier n'avait pas d'arêtes pré-enregistrées
			if (!plateau.getGraphe().aDesAretes())
			{
				plateau.getGraphe().genererAretesAuto(plateau);
			}

			return plateau;
		}
		catch (Exception e)
		{
			System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
			return null;
		}
	}
}
