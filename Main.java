# JavaProjectBinaryTreeDrawing
Binary Tree Drawing

package treePainter;

import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import java.awt.Button;

class TreeNode {
	int data;
	TreeNode left, right;
	static int maxDepth;

	public TreeNode(int d) {
		data = d;
		left = right = null;
	}

	public TreeNode(int d, TreeNode l, TreeNode r) {
		data = d;
		left = l;
		right = r;
	}

	public static void printTree(TreeNode node) {
		if (node != null) {
			printTree(node.left);
			System.out.println(node.data);
			printTree(node.right);
		}
	}

	public static TreeNode insertTree(TreeNode node, int d) {
		TreeNode last = null;
		int depth = 0;
		while (node != null) {
			last = node;
			if (d > node.data)
				node = node.right;
			else if (d < node.data)
				node = node.left;
			else
				return null;
			if (++depth > maxDepth)
				maxDepth = depth;
		}
		TreeNode newnode = new TreeNode(d);
		if (last != null) {
			if (d > last.data)
				last.right = newnode;
			else
				last.left = newnode;
		}
		return newnode;
	}

	// I added the search method here!
	// Starting the search algorithm

	public static TreeNode searchTree(TreeNode node, int d) {

		if (node == null || node.data == d) {

			return node;
		}
		if (node.data > d) {

			return searchTree(node.left, d);
		} else {

			return searchTree(node.right, d);
		}
	}

	// I added the delete method here!
	// Starting the delete algorithm
	public TreeNode deleteTree(TreeNode root, int d) {
		TreeNode node = root;
		if (node == null) {
			return node;

		} else if (d < node.data) {
			node.left = deleteTree(node.left, d);
		} else if (d > node.data) {
			node.right = deleteTree(node.right, d);
		} else {
			if (node.left == null || node.right == null) {
				TreeNode temp = null;
				temp = node.left == null ? node.right : node.left;
				if (temp == null) {
					return null;
				}
			} else {

				TreeNode temp = findMinFromRight(node.right);
				node.data = temp.data;
				node.right = deleteTree(node.right, temp.data);
				return node;
			}

		}
		return node;
	}

	private TreeNode findMinFromRight(TreeNode node) {
		if (node == null) {
			return null;
		}
		TreeNode temp = node.right;

		while (temp != null) {
			temp = temp.left;
		}
		return temp;
	}
}

public class Main {

	private JFrame frmTreePainter;
	private JTextField textField;
	public int levels;
	public int searchingNumber = 0;
	public boolean status = false;

	class painter extends JPanel {

		void drawTree(TreeNode node, Graphics g, int x, int y, int level) {
			if (level > 0 && node != null) {
				int d = 10 * (1 << level);
				drawTree(node.left, g, x - d, y + 30, level - 1);

				if (status == true && searchingNumber == node.data)
					g.setColor(Color.ORANGE);
				g.drawOval(x, y, 20, 20);
				if (node.left != null)
					g.drawLine(x + 10, y + 20, x + 10 - d, y + 30);
				if (node.right != null)
					g.drawLine(x + 10, y + 20, x + 10 + d, y + 30);
				g.drawString("" + node.data, x + 5, y + 15);
				g.setColor(Color.BLACK);
				drawTree(node.right, g, x + d, y + 30, level - 1);

			}
		}

		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);
			
			drawTree(tree, g, getWidth() / 2, 0, tree.maxDepth + 1);

		}

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmTreePainter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	TreeNode tree;
	private JTextField number;
	private JButton searchButton;

	private void initialize() {
		frmTreePainter = new JFrame();
		frmTreePainter.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Main.class.getResource("/javax/swing/plaf/basic/icons/image-delayed.png")));
		frmTreePainter.setTitle("Tree painter");
		frmTreePainter.setBounds(100, 100, 450, 300);
		frmTreePainter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frmTreePainter.getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Depth:");
		panel.add(lblNewLabel);

		textField = new JTextField();
		textField.setText("5");
		panel.add(textField);
		textField.setColumns(10);

		JButton paintButton = new JButton("Paint it!");
		paintButton.setFont(new Font("Tahoma", Font.ITALIC, 13));
		paintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("You clicked on me!!");
				String number = textField.getText();
				levels = Integer.parseInt(number);
				TreeNode t = TreeNode.insertTree(tree, levels);
				if (tree == null)
					tree = t;
				frmTreePainter.repaint();
				System.out.println("Number is=" + number);
			}
		});
		panel.add(paintButton);

		painter panel_1 = new painter();
		frmTreePainter.getContentPane().add(panel_1, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		frmTreePainter.getContentPane().add(panel_2, BorderLayout.SOUTH);

		// Starting Search!!!
		JLabel labelResult = new JLabel("");
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("You can select a number!!");
				TreeNode result = null;
				searchingNumber = Integer.parseInt(number.getText());
				result = tree.searchTree(tree, searchingNumber);

				if (result != null) {
					System.out.println(result.data);
					status = true;
					frmTreePainter.repaint();
					labelResult.setText("");
				} else {
					System.out.println("No found!");
					labelResult.setText("No found!");
				}

			}
		});

		number = new JTextField();
		panel_2.add(number);
		number.setColumns(10);
		searchButton.setFont(new Font("Tahoma", Font.ITALIC, 13));
		panel_2.add(searchButton);

		// Starting Delete!!!

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TreeNode result = null;
				searchingNumber = Integer.parseInt(number.getText());

				tree.deleteTree(tree, searchingNumber);
				frmTreePainter.repaint();
			}
		});
		deleteButton.setFont(new Font("Tahoma", Font.ITALIC, 13));
		panel_2.add(deleteButton);

		labelResult.setForeground(Color.ORANGE);
		panel_2.add(labelResult);

	}
}
