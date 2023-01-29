import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTextField;

public class BookBuyerGui extends JFrame {

	private JPanel contentPane;
	private JLabel tituloLivrosAVenda;
	private JLabel livroTitulo_1;
	private JButton btnComprar_1;
	private JScrollPane scrollPane;
	private JList listaLivrosComponent;
	private JLabel tituloLivroDesejaComprar;
	private JTextField inputLivro;
	private JButton btnComprar;
	
	private BookBuyerAgent myAgent;

	/**
	 * Create the frame.
	 */
	public BookBuyerGui(BookBuyerAgent a) {
		super(a.getLocalName());
		
		myAgent = a;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 386, 446);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tituloLivrosAVenda = new JLabel("Livros a Venda");
		tituloLivrosAVenda.setHorizontalAlignment(SwingConstants.CENTER);
		tituloLivrosAVenda.setFont(new Font("Tahoma", Font.BOLD, 20));
		tituloLivrosAVenda.setBounds(0, 11, 348, 25);
		contentPane.add(tituloLivrosAVenda);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 47, 370, 216);
		contentPane.add(scrollPane);
		
		listaLivrosComponent = new JList();
		listaLivrosComponent.setModel(new AbstractListModel() {
			String[] values = myAgent.getBookTitles();
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane.setViewportView(listaLivrosComponent);
		
		tituloLivroDesejaComprar = new JLabel("Digite qual livro deseja comprar:");
		tituloLivroDesejaComprar.setHorizontalAlignment(SwingConstants.CENTER);
		tituloLivroDesejaComprar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tituloLivroDesejaComprar.setBounds(0, 274, 370, 25);
		contentPane.add(tituloLivroDesejaComprar);
		
		inputLivro = new JTextField();
		inputLivro.setBounds(52, 310, 260, 20);
		contentPane.add(inputLivro);
		inputLivro.setColumns(10);
		
		btnComprar = new JButton("Comprar");
		btnComprar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myAgent.performBuyRequest(inputLivro.getText());
			}
		});
		btnComprar.setBounds(135, 353, 89, 23);
		contentPane.add(btnComprar);
	}
	
	public void showGui() {
        pack();
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int centerX = (int) screenSize.getWidth() / 2;
//        int centerY = (int) screenSize.getHeight() / 2;
//        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}