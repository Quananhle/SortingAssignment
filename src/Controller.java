import javax.swing.JFrame;

/**
 * Controller class for the project, contains the main method that creates and runs
 * mainForm using an anonymous inner class
 * @author Oliver Palmer, stuNumber 12089466
 *
 */
public class Controller {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				mainForm f = new mainForm();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setSize(750,450);
				f.setVisible(true);
			}
		});

	}

}
