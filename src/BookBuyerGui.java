import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BookBuyerGui extends JFrame {

	private JPanel contentPane;
	private JLabel tituloLivrosAVenda;
	private JLabel livroTitulo_1;
	private JButton btnComprar_1;
	private JScrollPane scrollPane;
	private JList listaLivrosComponent;
	private JLabel tituloLivroDesejaComprar;
	private JLabel proposalLabel;
	private JLabel qualidadeLivroDesejaComprar;
	private JTextField inputLivro;
	private JTextField inputProposal;
	private JTextField inputQuality;
	private JButton btnComprar;
	
	private BookBuyerAgent myAgent;

	/**
	 * Create the frame.
	 */
	public BookBuyerGui(BookBuyerAgent a) {
		super(a.getLocalName());
		
		myAgent = a;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 386, 510);
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

		proposalLabel = new JLabel("Digite o preco maximo que deseja pagar:");
		proposalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		proposalLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		proposalLabel.setBounds(0, 330, 370, 25);
		contentPane.add(proposalLabel);

		inputProposal = new JTextField();
		inputProposal.setBounds(52, 360, 260, 20);
		contentPane.add(inputProposal);
		inputProposal.setColumns(10);
		
		qualidadeLivroDesejaComprar = new JLabel("Digite qualidade do livro que deseja comprar:");
		qualidadeLivroDesejaComprar.setHorizontalAlignment(SwingConstants.CENTER);
		qualidadeLivroDesejaComprar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		qualidadeLivroDesejaComprar.setBounds(0, 380, 370, 25);
		contentPane.add(qualidadeLivroDesejaComprar);
		
		inputQuality = new JTextField();
		inputQuality.setBounds(52, 410, 260, 20);
		contentPane.add(inputQuality);
		inputQuality.setColumns(10);
		
		btnComprar = new JButton("Comprar");
		btnComprar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] values =  myAgent.getBookTitles();
				int found = 0;
				int priceOk = 0;
				for (int i = 0; i < values.length; i++) {
					if (values[i].equals(inputLivro.getText())) {
						found = 1;
					}
				}

				try {
					Integer.parseInt(inputProposal.getText());
					priceOk = 1;
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(BookBuyerGui.this, "Valor inv??lido", "Error", JOptionPane.ERROR_MESSAGE);
				}

				if (found == 1 && priceOk == 1) myAgent.performBuyRequest(inputLivro.getText(), inputProposal.getText(), inputQuality.getText());
				else JOptionPane.showMessageDialog(BookBuyerGui.this, "Livro n??o encontrado", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnComprar.setBounds(135, 440, 89, 23);
		contentPane.add(btnComprar);
	}
	
	public void showGui() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}