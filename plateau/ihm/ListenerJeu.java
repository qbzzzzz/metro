package plateau.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ListenerJeu implements ActionListener
{
	private PanelJeu panel;

	public ListenerJeu(PanelJeu panel)
	{
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == panel.getBtnImporter())
		{
			Object selectedItem = panel.getComboFichiersImport().getSelectedItem();
			if (selectedItem != null)
			{
				File file = new File("sauvegarde", selectedItem.toString());
				if (file.exists()) panel.chargerApercu(file);
			}
		}
		if (e.getSource() == panel.getBtnCreerNouveau())
		{
			new FrameConfiguration(panel.getCtrl());
			panel.getFrame().dispose();
		}
		if (e.getSource() == panel.getBtnSauvegarder() && panel.getFichierCharge() != null)
		{
			if (panel.getCtrl().validerDepartsPlateau())
			{
				if (panel.getCtrl().enregistrerPlateau(panel.getFichierCharge().getName()))
				{
					panel.getFrame().dispose();
				}
			}
		}
	}
}
