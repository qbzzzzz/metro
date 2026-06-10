package plateau.metier;

import java.util.ArrayList;

// Réseau (ligne de métro) d'un joueur : la suite ordonnée des stations posées.
public class ReseauJoueur
{
	private ArrayList<Integer> stations;

	public ReseauJoueur()
	{
		this.stations = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getStations() { return this.stations; }

	public void ajouterStation(int numCase)
	{
		if (!this.contient(numCase))
			this.stations.add(numCase);
	}

	public boolean contient(int numCase)
	{
		for (int i = 0; i < this.stations.size(); i++)
		{
			if (this.stations.get(i) == numCase) return true;
		}
		return false;
	}

	// Retourne la dernière station ajoutée (celle depuis laquelle on peut prolonger la ligne)
	public int getDerniereStation()
	{
		if (this.stations.isEmpty()) return -1;
		return this.stations.get(this.stations.size() - 1);
	}
}
