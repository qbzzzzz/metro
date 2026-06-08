package plateau.ihm;

import plateau.Controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Écran de dessin du plateau permettant d'assigner des arrondissements (couleurs) aux cases à la souris
public class PanelCreation extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{
    // Attributs : Fenêtres, contrôleur et sous-panneaux (Grille, Liste arrondissements, Actions)
    private FrameConfiguration          frmConfiguration;
    private FrameCreation               frmCreation;
    private Controleur                  ctrl;

    private JPanel                      panelPlateau;
    private JPanel                      panelArrondissments;
    private JPanel                      panelBoutons;
    
    // Tableaux stockant les cases physiques (JPanel) et les données issues de la configuration précédente
    private JPanel[] tabCases           = new JPanel[ Integer.parseInt(PanelConfiguration.txtLargeur.getText()) * Integer.parseInt(PanelConfiguration.txtHauteur.getText()) ];

    private int grillelargeur           = Integer.parseInt(PanelConfiguration.txtLargeur        .getText());
    private int grillehauteur           = Integer.parseInt(PanelConfiguration.txtHauteur        .getText());
    private int nombreArrondissments    = Integer.parseInt(PanelConfiguration.txtArrondissments .getText());
    private int tailleCases             = Integer.parseInt(PanelConfiguration.txtTailleCases    .getText());

    private JButton[]                   btnArrondissments = new JButton[ nombreArrondissments ];

    // Composants de gestion de fichier et d'action
    private JButton                     btnValider;
    private JButton                     btnRetourConfiguration;
    private JButton                     btnPasserAuJeu;
    private JTextField                  txtNomFichier;

    // État courant : Stocke l'arrondissement et la couleur actuellement sélectionnés par l'utilisateur
    private int                         arrondissementSelectionne = 0;
    private Color                       couleurSelectionnee = Color.LIGHT_GRAY;

    // Tableaux de données fixes : Noms officiels et palette de 20 couleurs distinctes
    private String[]                    tabNomsArrondissments = {"1er","2ème","3ème","4ème","5ème","6ème","7ème","8ème","9ème","10ème","11ème","12ème","13ème","14ème","15ème","16ème","17ème","18ème","19ème","20ème"};
    private Color[] tabCouleurs = 
    {
        new Color(255, 105, 180), new Color(138, 43, 226),  new Color(255, 165, 0),   new Color(0, 255, 255),
        new Color(176, 196, 222), new Color(255, 20, 147),  new Color(186, 85, 211),  new Color(148, 0, 211),
        new Color(210, 105, 30),  new Color(244, 164, 96),  new Color(199, 21, 133),  new Color(123, 104, 238),
        new Color(255, 140, 0),   new Color(0, 206, 209),   new Color(72, 61, 139),   new Color(220, 20, 60),
        new Color(169, 169, 169), new Color(245, 222, 179), new Color(255, 228, 225), new Color(255, 192, 203)
    };

    // Constructeur : Crée l'interface, génère la grille de cases neutres et active les écouteurs souris/boutons
    public PanelCreation(FrameCreation frmCreation, Controleur ctrl)
    {
        this.frmCreation            = frmCreation;
        this.ctrl                   = ctrl;
        
        this.setLayout(new BorderLayout());

        this.panelPlateau           = new JPanel(new GridLayout(this.grillehauteur, this.grillelargeur, 0, 0));
        this.panelArrondissments    = new JPanel(new GridLayout(this.nombreArrondissments, 1, 5, 5));
        this.panelBoutons           = new JPanel(new GridLayout(2, 1, 5, 5));

        /*-------------------------*/
        /* Création des composants */
        /*-------------------------*/

        // Génération dynamique de la grille (cases initialement gris clair)
        for (int i = 0; i < this.grillelargeur * this.grillehauteur; i++)
        {
            tabCases[i]             = new JPanel();
            tabCases[i]             .setBackground(Color.LIGHT_GRAY);
            tabCases[i]             .setBorder(BorderFactory.createLineBorder(Color.BLACK));
            tabCases[i]             .setPreferredSize(new Dimension(this.tailleCases, this.tailleCases));
        }   

        // Génération des boutons de sélection d'arrondissement avec leur couleur associée
        for (int i = 0; i < this.nombreArrondissments; i++)
        {
            btnArrondissments[i]    = new JButton( this.tabNomsArrondissments[i] + " Arrondissement" );
            btnArrondissments[i]    .setBackground( this.tabCouleurs[i] );
        }

        this.btnValider             = new JButton("Valider");
        this.btnRetourConfiguration = new JButton("Retour à la configuration");
        this.btnPasserAuJeu         = new JButton("Importation Uniquement");

        // Zone de saisie pour le nom du fichier de sauvegarde
        JPanel panelNom             = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblNom               = new JLabel("Nom du plateau :");
        this.txtNomFichier          = new JTextField(15);
        panelNom.add(lblNom);
        panelNom.add(this.txtNomFichier);

        // Alignement horizontal des boutons du bas
        JPanel panelBtns            = new JPanel(new GridLayout(1, 3, 5, 5));
        panelBtns                   .add(this.btnValider);
        panelBtns                   .add(this.btnRetourConfiguration);
        panelBtns                   .add(this.btnPasserAuJeu);

        this.panelBoutons           .add(panelNom);
        this.panelBoutons           .add(panelBtns);

        /*-------------------------------*/
        /* Positionnement des composants */
        /*-------------------------------*/
        this.add(this.panelPlateau,         BorderLayout.CENTER);
        this.add(this.panelArrondissments,  BorderLayout.WEST);

        for (int i = 0; i < this.grillelargeur * this.grillehauteur; i++) { this.panelPlateau.add(tabCases[i]); }
        for (int i = 0; i < this.nombreArrondissments; i++) { this.panelArrondissments.add(btnArrondissments[i]); }

        this.add(this.panelBoutons, BorderLayout.SOUTH);

        /*---------------------------*/
        /* Activation des composants */
        /*---------------------------*/
        this.panelPlateau           .addMouseListener(this);
        this.panelPlateau           .addMouseMotionListener(this);

        for (int i = 0; i < this.nombreArrondissments; i++) { btnArrondissments[i].addActionListener(this); }

        this.btnValider             .addActionListener(this);
        this.btnRetourConfiguration .addActionListener(this);
        this.btnPasserAuJeu         .addActionListener(this);
    }

    // Méthode outil : Identifie la case survolée/cliquée et lui applique l'arrondissement sélectionné
    private void colorierCaseSousSouris(MouseEvent e)
    {
        Component comp = this.panelPlateau.getComponentAt(e.getPoint());
        if (comp instanceof JPanel && comp != this.panelPlateau)
        {
            JPanel casePanel = (JPanel) comp;
            for (int i = 0; i < this.tabCases.length; i++)
            {
                if (this.tabCases[i] == casePanel)
                {
                    casePanel.setBackground(this.couleurSelectionnee);
                    this.ctrl.affecterArrondissement(i, this.arrondissementSelectionne); // Envoi au modèle
                    break;
                }
            }
        }
    }

    // Déclencheurs Souris : Colorie au simple clic ou lors du glisser (permet de "peindre" le plateau)
    public void mousePressed(MouseEvent e)  { colorierCaseSousSouris(e); }
    public void mouseDragged(MouseEvent e)  { colorierCaseSousSouris(e); }

    // Événements souris non utilisés requis par les interfaces
    public void mouseMoved(MouseEvent e)    {}
    public void mouseClicked(MouseEvent e)  {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e)  {}
    public void mouseExited(MouseEvent e)   {}

    // Gestion des clics sur les boutons (Arrondissements et boutons d'action)
    public void actionPerformed(ActionEvent e)
    {
        // 1. Sélection d'un arrondissement : Met à jour la couleur active et l'index de l'arrondissement
        for (int i = 0; i < nombreArrondissments; i++)
        {
            if (e.getSource() == btnArrondissments[i])
            {
                couleurSelectionnee = btnArrondissments[i].getBackground();
                this.arrondissementSelectionne = i + 1;
            }
        }

        // 2. Action Valider : Vérifie que le plateau est entièrement peint, valide le nom, sauvegarde et lance le jeu
        if (e.getSource() == this.btnValider)
        {
            for (int i = 0; i < this.tabCases.length; i++)
            {
                if (this.tabCases[i].getBackground().equals(Color.LIGHT_GRAY))
                {
                    System.out.println("Veuillez remplir toutes les cases du plateau avant de valider.");
                    return;
                }
            }

            String nomPlateau = this.txtNomFichier.getText();
            if (nomPlateau == null || nomPlateau.trim().isEmpty())
            {
                System.out.println("Le nom de fichier ne peut pas être vide.");
                return;
            }

            if (this.ctrl.enregistrerPlateau(nomPlateau.trim()))
            {
                System.out.println("Plateau \"" + nomPlateau.trim() + "\" sauvegardé !");
                new FrameJeu(this.ctrl);
                this.frmCreation.setVisible(false);
            }
        }

        // 3. Action Retour : Rouvre l'écran de configuration
        if (e.getSource() == this.btnRetourConfiguration)
        {
            if (this.frmConfiguration == null) { this.frmConfiguration = new FrameConfiguration(this.ctrl); }
            this.frmConfiguration.setVisible(true);
            this.frmCreation.setVisible(false);
        }

        // 4. Action Passer au jeu : Lance l'écran de jeu directement (sans sauvegarder le dessin actuel)
        if (e.getSource() == this.btnPasserAuJeu)
        {
            new FrameJeu(this.ctrl);
            this.frmCreation.setVisible(false);
        }
    }
}