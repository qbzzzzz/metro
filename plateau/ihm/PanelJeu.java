package plateau.ihm;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import plateau.Controleur;
import plateau.metier.GestionnairePlacement;
import plateau.metier.UtilitaireJeu;

public class PanelJeu extends JPanel
{
	private FrameJeu frame;
	private Controleur ctrl;
	private File fichierCharge;

	private JPanel panelGauche;
	private JPanel panelDroite;
	private JPanel panelApercuGrille;

	private JLabel lblJoueurs;
	private JLabel lblStations;
	private JLabel lblAretes;
	private JLabel lblApercuTitre;

	private JRadioButton rbStation;
	private JRadioButton rbDepart;
	private JComboBox<String> comboStation;
	private JComboBox<String> comboDepart;
	private JButton btnSauvegarder;

	private JComboBox<String> comboFichiersImport;
	private JButton btnImporter;
	private JButton btnCreerNouveau;

	private Image[] stationImages = new Image[11];
	private ListenerJeu ecouteur;

	public PanelJeu(FrameJeu frame, Controleur ctrl)
	{
		this.frame = frame;
		this.ctrl = ctrl;
		this.ecouteur = new ListenerJeu(this);

		this.setPreferredSize(new Dimension(900, 640));
		this.setLayout(new BorderLayout(20, 20));
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		this.panelGauche = new JPanel();
		this.panelGauche.setLayout(new BoxLayout(this.panelGauche, BoxLayout.Y_AXIS));
		this.panelGauche.setBorder(BorderFactory.createTitledBorder("Informations du Jeu"));
		this.panelGauche.setPreferredSize(new Dimension(280, 580));

		this.lblJoueurs = new JLabel("Nombre de joueurs : " + this.ctrl.getNbJoueurs());
		this.lblStations  = new JLabel("Nombre de stations : " + this.ctrl.getNbStations());
		this.lblAretes = new JLabel("Nombre d'arêtes : 0");

		Font fontLabel = new Font("Arial", Font.BOLD, 14);
		this.lblJoueurs.setFont(fontLabel);
		this.lblStations.setFont(fontLabel);
		this.lblAretes.setFont(fontLabel);

		this.lblJoueurs.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.lblStations.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.lblAretes.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.panelGauche.add(this.lblJoueurs);
		this.panelGauche.add(Box.createVerticalStrut(10));
		this.panelGauche.add(this.lblStations);
		this.panelGauche.add(Box.createVerticalStrut(10));
		this.panelGauche.add(this.lblAretes);
		this.panelGauche.add(Box.createVerticalStrut(20));

		JLabel lblFichier = new JLabel("Fichier du plateau :");
		lblFichier.setFont(new Font("Arial", Font.BOLD, 12));
		lblFichier.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.panelGauche.add(lblFichier);
		this.panelGauche.add(Box.createVerticalStrut(5));

		this.comboFichiersImport = new JComboBox<>();
		this.comboFichiersImport.setMaximumSize(new Dimension(260, 30));
		this.comboFichiersImport.setAlignmentX(Component.LEFT_ALIGNMENT);
		mettreAJourComboFichiers();
		this.panelGauche.add(this.comboFichiersImport);
		this.panelGauche.add(Box.createVerticalStrut(5));

		this.btnImporter = new JButton("Charger le plateau");
		this.btnImporter.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.btnImporter.addActionListener(this.ecouteur);
		this.panelGauche.add(this.btnImporter);
		this.panelGauche.add(Box.createVerticalStrut(10));

		this.btnCreerNouveau = new JButton("Créer un nouveau plateau");
		this.btnCreerNouveau.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.btnCreerNouveau.addActionListener(this.ecouteur);
		this.panelGauche.add(this.btnCreerNouveau);
		this.panelGauche.add(Box.createVerticalStrut(20));

		JSeparator sep = new JSeparator();
		sep.setMaximumSize(new Dimension(260, 10));
		sep.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.panelGauche.add(sep);
		this.panelGauche.add(Box.createVerticalStrut(15));

		JLabel lblPlacement = new JLabel("Mode de placement :");
		lblPlacement.setFont(new Font("Arial", Font.BOLD, 14));
		lblPlacement.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.panelGauche.add(lblPlacement);
		this.panelGauche.add(Box.createVerticalStrut(10));

		this.rbStation = new JRadioButton("Placer une Station :", true);
		this.rbDepart = new JRadioButton("Placer un Départ :");
		ButtonGroup group = new ButtonGroup();
		group.add(this.rbStation);
		group.add(this.rbDepart);

		this.rbStation.setFont(new Font("Arial", Font.BOLD, 12));
		this.rbStation.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.rbDepart.setFont(new Font("Arial", Font.BOLD, 12));
		this.rbDepart.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.comboStation = new JComboBox<>();
		this.comboStation.setMaximumSize(new Dimension(260, 30));
		this.comboStation.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.comboDepart = new JComboBox<>();
		this.comboDepart.setMaximumSize(new Dimension(260, 30));
		this.comboDepart.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.rbStation.addActionListener(e -> {
			this.comboStation.setEnabled(true);
			this.comboDepart.setEnabled(false);
		});
		this.rbDepart.addActionListener(e -> {
			this.comboStation.setEnabled(false);
			this.comboDepart.setEnabled(true);
		});

		this.comboStation.setEnabled(true);
		this.comboDepart.setEnabled(false);

		mettreAJourComboPlacement();

		this.panelGauche.add(this.rbStation);
		this.panelGauche.add(Box.createVerticalStrut(5));
		this.panelGauche.add(this.comboStation);
		this.panelGauche.add(Box.createVerticalStrut(15));
		this.panelGauche.add(this.rbDepart);
		this.panelGauche.add(Box.createVerticalStrut(5));
		this.panelGauche.add(this.comboDepart);
		this.panelGauche.add(Box.createVerticalStrut(30));

		this.btnSauvegarder = new JButton("Sauvegarder les modifications");
		this.btnSauvegarder.setFont(new Font("Arial", Font.BOLD, 12));
		this.btnSauvegarder.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.btnSauvegarder.setEnabled(false);
		this.btnSauvegarder.addActionListener(this.ecouteur);
		this.panelGauche.add(this.btnSauvegarder);

		this.panelDroite = new JPanel(new BorderLayout(10, 10));
		this.panelDroite.setBorder(BorderFactory.createTitledBorder("Plateau Interactif"));

		this.lblApercuTitre = new JLabel("Aucun plateau chargé", SwingConstants.CENTER);
		this.lblApercuTitre.setFont(new Font("Arial", Font.ITALIC, 14));
		this.panelDroite.add(this.lblApercuTitre, BorderLayout.NORTH);

		this.panelApercuGrille = new JPanel()
		{
			@Override
			public void paint(Graphics g)
			{
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setStroke(new BasicStroke(3));
				g2d.setColor(new Color(160, 160, 160, 200));

				int size = ctrl.getLargeur() * ctrl.getHauteur();
				if (getComponentCount() == size)
				{
					for (int i = 0; i < size; i++)
					{
						for (int j = i + 1; j < size; j++)
						{
							if (ctrl.aArete(i, j))
							{
								Component compI = getComponent(i);
								Component compJ = getComponent(j);

								int x1 = compI.getX() + compI.getWidth() / 2;
								int y1 = compI.getY() + compI.getHeight() / 2;
								int x2 = compJ.getX() + compJ.getWidth() / 2;
								int y2 = compJ.getY() + compJ.getHeight() / 2;

								g2d.drawLine(x1, y1, x2, y2);
							}
						}
					}
				}
			}
		};
		this.panelApercuGrille.setBackground(Color.GRAY);
		this.panelApercuGrille.setLayout(new GridBagLayout());
		
		JLabel lblInfo = new JLabel("<html><center>Sélectionnez une sauvegarde à gauche et cliquez sur <b>Charger le plateau</b></center></html>");
		this.panelApercuGrille.add(lblInfo);

		this.panelDroite.add(this.panelApercuGrille, BorderLayout.CENTER);

		this.add(this.panelGauche, BorderLayout.WEST);
		this.add(this.panelDroite, BorderLayout.CENTER);
	}

	// GETTERS pour l'EcouteurJeu
	public JButton getBtnImporter() { return this.btnImporter; }
	public JButton getBtnCreerNouveau() { return this.btnCreerNouveau; }
	public JButton getBtnSauvegarder() { return this.btnSauvegarder; }
	public JComboBox<String> getComboFichiersImport() { return this.comboFichiersImport; }
	public Controleur getCtrl() { return this.ctrl; }
	public FrameJeu getFrame() { return this.frame; }
	public File getFichierCharge() { return this.fichierCharge; }

	private void mettreAJourComboFichiers()
	{
		this.comboFichiersImport.removeAllItems();
		File dir = new File("sauvegarde");
		if (dir.exists() && dir.isDirectory())
		{
			File[] list = dir.listFiles();
			if (list != null)
			{
				for (File f : list)
				{
					if (f.isFile() && f.getName().endsWith(".txt"))
					{
						this.comboFichiersImport.addItem(f.getName());
					}
				}
			}
		}
	}

	private void mettreAJourComboPlacement()
	{
		if (this.comboStation == null || this.comboDepart == null) return;

		this.comboStation.removeAllItems();
		this.comboStation.addItem("Aucun");
		int nbStations = this.ctrl.getNbStations();
		String[] nomsStations = UtilitaireJeu.getNomsStations();
		for (int i = 1; i <= nbStations; i++)
		{
			if (i <= nomsStations.length)
			{
				this.comboStation.addItem(nomsStations[i - 1]);
			}
			else
			{
				this.comboStation.addItem("Station " + i);
			}
		}

		this.comboDepart.removeAllItems();
		this.comboDepart.addItem("Aucun");
		int nbJoueurs = this.ctrl.getNbJoueurs();
		for (int i = 1; i <= nbJoueurs; i++)
		{
			this.comboDepart.addItem("Joueur " + i);
		}
	}

	public void chargerApercu(File file)
	{
		if (this.ctrl.chargerPlateau(file))
		{
			this.fichierCharge = file;
			this.ctrl.mettreAJourConfigurationDepuisPlateau();

			this.lblJoueurs.setText("Nombre de joueurs : " + this.ctrl.getNbJoueurs());
			this.lblStations.setText("Nombre de stations : " + this.ctrl.getNbStations());
			this.lblAretes.setText("Nombre d'arêtes : " + this.ctrl.getNbAretes());

			mettreAJourComboPlacement();
			this.btnSauvegarder.setEnabled(true);

			this.panelApercuGrille.removeAll();
			int largeur = this.ctrl.getLargeur();
			int hauteur = this.ctrl.getHauteur();
			this.panelApercuGrille.setLayout(new GridLayout(hauteur, largeur, 2, 2));
			this.panelApercuGrille.setBackground(Color.DARK_GRAY);

			int size = largeur * hauteur;
			for (int i = 0; i < size; i++)
			{
				this.panelApercuGrille.add(new CasePanel(i, this.ctrl, this));
			}

			// Restauration de l'état normal du titre
			this.lblApercuTitre.setText("Plateau : " + file.getName());
			this.lblApercuTitre.setFont(new Font("Arial", Font.BOLD, 14));
			this.lblApercuTitre.setForeground(Color.BLACK); 

			this.panelApercuGrille.revalidate();
			this.panelApercuGrille.repaint();
			this.frame.pack();
			this.frame.setLocationRelativeTo(null);
		}
		else
		{
			// ERREUR GÉRÉE SANS JOPTIONPANE : Modification visuelle directe
			this.lblApercuTitre.setText("ÉCHEC DE CHARGEMENT");
			this.lblApercuTitre.setFont(new Font("Arial", Font.BOLD, 14));
			this.lblApercuTitre.setForeground(Color.RED); // Titre passe en rouge anonyme

			this.panelApercuGrille.removeAll();
			this.panelApercuGrille.setLayout(new GridBagLayout());
			this.panelApercuGrille.setBackground(new Color(255, 230, 230)); // Fond d'aperçu légèrement rosé

			JLabel lblErreur = new JLabel("<html><center><font color='red'><b>Plateau invalide !</b></font><br><br>"
					+ "Le fichier <b>" + file.getName() + "</b> n'a pas pu être chargé car il contient<br>"
					+ "des structures d'arêtes invalides ou des valeurs hors-bornes.</center></html>");
			lblErreur.setFont(new Font("Arial", Font.PLAIN, 13));
			
			this.panelApercuGrille.add(lblErreur);
			
			// Désactivation des fonctionnalités d'édition pour le fichier cassé
			this.btnSauvegarder.setEnabled(false);
			this.fichierCharge = null;

			this.panelApercuGrille.revalidate();
			this.panelApercuGrille.repaint();
		}
	}

	public void caseCliquee(int index)
	{
		String mode = "STATION";
		Object selection = this.comboStation.getSelectedItem();
		if (this.rbDepart.isSelected())
		{
			mode = "DEPART";
			selection = this.comboDepart.getSelectedItem();
		}

		GestionnairePlacement.gererClic(index, mode, selection, this.ctrl);

		this.ctrl.genererAretesAuto();
		this.lblAretes.setText("Nombre d'arêtes : " + this.ctrl.getNbAretes());
		this.panelApercuGrille.repaint();
	}

	public Image getImageStation(int stationNum)
	{
		if (stationNum < 1 || stationNum >= stationImages.length) return null;
		if (stationImages[stationNum] == null)
		{
			String chemin = UtilitaireJeu.getCheminImageStation(stationNum);
			if (chemin != null)
			{
				stationImages[stationNum] = new ImageIcon(chemin).getImage();
			}
		}
		return stationImages[stationNum];
	}
}
