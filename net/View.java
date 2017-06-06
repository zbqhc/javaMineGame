package net;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

public class View extends JFrame {

	int[][] gr;// 扫雷地图
	int victoryFlag = 0;// 雷场生成标记
	private int VICTORY = 0;// 胜利标志
	private int W;// 雷场宽度
	private int H;// 雷场高度
	private int mines;// 总雷数
	private int blockCounter;// 已开块计数器
	JButton[][] buttons;
	private Controller controller;

	public int getMines() {
		return mines;
	}

	public Controller getController() {
		return controller;
	}

	public void setW(int w) {
		W = w;
	}

	public void setH(int h) {
		H = h;
	}

	public void setMines(int mines) {
		this.mines = mines;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	// 封装
	public int getBlockCounter() {
		return blockCounter;
	}

	// 封装
	public int getW() {
		return W;
	}

	// 封装
	public int getH() {
		return H;
	}

	// 设置已开块初始值
	private void setBlockCounter(int blockCounter) {
		this.blockCounter = blockCounter;
	}

	// 已开块计数器刷新
	public void setBlockCounter() {
		this.blockCounter--;
//		System.out.println(blockCounter);
	}

	// 构造方法
	public View(Ground ground, int w, int h, int mines, Controller controller) {
		// TODO 自动生成的构造函数存根
		this.W = w;
		this.H = h;
		this.mines = mines;
		this.setBlockCounter(w * h);
		this.setController(controller);
		controller.setView(this);

		init(ground);
	}

	// 胜利消息框
	void victoryMsg() {

		if ((this.blockCounter == this.mines && this.VICTORY == 0)||(controller.getMineFlag()==this.mines)) {
			
			
			this.VICTORY = -1;
			JOptionPane.showMessageDialog(null, "扫雷成功\n耗时："+Math.abs((controller.getTimeFlag()-System.currentTimeMillis())/1000.0)+"秒\n\n", "恭喜",
					JOptionPane.INFORMATION_MESSAGE);
			
			
		}
	}

	// 数字染色
	static void setNumColor(JButton btn) {
		if (btn.getText().equals("1")) {
			btn.setForeground(new Color(0x0000ff));
		}
		if (btn.getText().equals("2")) {
			btn.setForeground(new Color(0x008000));
		}
		if (btn.getText().equals("3")) {
			btn.setForeground(new Color(0xff0000));
		}
		if (btn.getText().equals("4")) {
			btn.setForeground(new Color(0x000080));
		}
		if (btn.getText().equals("5")) {
			btn.setForeground(new Color(0x800000));
		}
		if (btn.getText().equals("6")) {
			btn.setForeground(new Color(0x008080));
		}
		if (btn.getText().equals("7")) {
			btn.setForeground(new Color(0x000000));
		}
		if (btn.getText().equals("8")) {
			btn.setForeground(new Color(0x808080));
		}
		btn.setFont(new Font("黑体", Font.BOLD, 24));

	}

	static int collAttr(JButton btn, int key) {
		if (btn == null) {
			//System.out.println("collAttr方法:重载2\t" + "参数NULL");
			return 0;
		}
		String item = btn.getName().split("\\|")[key];
		if (item.equals("")) {
			//System.out.println("collAttr方法:重载2\t" + "获取为空");
			return 0;
		} else {
			return Integer.parseInt(item);
		}
	}

	static void collAttr(JButton btn, int key, int value) {
		btn.setName(collAttr(btn, 0) + "|" + collAttr(btn, 1) + "|"
				+ String.valueOf(value));
		//System.out.println("collAttr方法:重载3\t" + collAttr(btn, 0) + "|"
		//		+ collAttr(btn, 1) + "|" + String.valueOf(value));
		return;
	}

	// 开启连续空白块
	int blankBlock(JButton[][] buttons, int x, int y) {
		int counter = 0;
		if (x >= 1 && x <= W && y >= 1 && y <= H && gr[x][y] == 0
				&& collAttr(buttons[x][y], 2) == 0) {
			collAttr(buttons[x][y], 2, -3);
			setBlockCounter();
			buttons[x][y].setText(" ");
			buttons[x][y].setBackground(Color.lightGray);

			if (gr[x - 1][y - 1] > 0 && collAttr(buttons[x - 1][y - 1], 2) == 0) {
				collAttr(buttons[x - 1][y - 1], 2, gr[x - 1][y - 1]);
				buttons[x - 1][y - 1].setText(String.valueOf(gr[x - 1][y - 1]));
				setNumColor(buttons[x - 1][y - 1]);
				buttons[x - 1][y - 1].setBackground(Color.LIGHT_GRAY);
				setBlockCounter();
			}
			if (gr[x][y - 1] > 0 && collAttr(buttons[x][y - 1], 2) == 0) {
				collAttr(buttons[x][y - 1], 2, gr[x][y - 1]);
				buttons[x][y - 1].setText(String.valueOf(gr[x][y - 1]));
				setNumColor(buttons[x][y - 1]);
				buttons[x][y - 1].setBackground(Color.LIGHT_GRAY);
				setBlockCounter();
			}
			if (gr[x + 1][y - 1] > 0 && collAttr(buttons[x + 1][y - 1], 2) == 0) {
				collAttr(buttons[x + 1][y - 1], 2, gr[x + 1][y - 1]);
				buttons[x + 1][y - 1].setText(String.valueOf(gr[x + 1][y - 1]));
				setNumColor(buttons[x + 1][y - 1]);
				buttons[x + 1][y - 1].setBackground(Color.LIGHT_GRAY);
				setBlockCounter();
			}
			if (gr[x - 1][y] > 0 && collAttr(buttons[x - 1][y], 2) == 0) {
				collAttr(buttons[x - 1][y], 2, gr[x - 1][y]);
				buttons[x - 1][y].setText(String.valueOf(gr[x - 1][y]));
				setNumColor(buttons[x - 1][y]);
				buttons[x - 1][y].setBackground(Color.LIGHT_GRAY);
				setBlockCounter();
			}
			if (gr[x + 1][y] > 0 && collAttr(buttons[x + 1][y], 2) == 0) {
				collAttr(buttons[x + 1][y], 2, gr[x + 1][y]);
				buttons[x + 1][y].setText(String.valueOf(gr[x + 1][y]));
				setNumColor(buttons[x + 1][y]);
				buttons[x + 1][y].setBackground(Color.LIGHT_GRAY);
				setBlockCounter();
			}
			if (gr[x - 1][y + 1] > 0 && collAttr(buttons[x - 1][y + 1], 2) == 0) {
				collAttr(buttons[x - 1][y + 1], 2, gr[x - 1][y + 1]);
				buttons[x - 1][y + 1].setText(String.valueOf(gr[x - 1][y + 1]));
				setNumColor(buttons[x - 1][y + 1]);
				buttons[x - 1][y + 1].setBackground(Color.LIGHT_GRAY);
				setBlockCounter();
			}
			if (gr[x][y + 1] > 0 && collAttr(buttons[x][y + 1], 2) == 0) {
				collAttr(buttons[x][y + 1], 2, gr[x][y + 1]);
				buttons[x][y + 1].setText(String.valueOf(gr[x][y + 1]));
				setNumColor(buttons[x][y + 1]);
				buttons[x][y + 1].setBackground(Color.LIGHT_GRAY);
				setBlockCounter();
			}
			if (gr[x + 1][y + 1] > 0 && collAttr(buttons[x + 1][y + 1], 2) == 0) {
				collAttr(buttons[x + 1][y + 1], 2, gr[x + 1][y + 1]);
				buttons[x + 1][y + 1].setText(String.valueOf(gr[x + 1][y + 1]));
				setNumColor(buttons[x + 1][y + 1]);
				buttons[x + 1][y + 1].setBackground(Color.LIGHT_GRAY);
				setBlockCounter();
			}
			blankBlock(buttons, x - 1, y);
			blankBlock(buttons, x + 1, y);
			blankBlock(buttons, x, y - 1);
			blankBlock(buttons, x, y + 1);
			victoryMsg();
		}

		return counter;

	}

	// 初始化雷场
	void init(final Ground ground) {
		setLayout(new GridLayout(getW(), getH(), 3, 3));
		setTitle("JavaMineSweeper");
		setResizable(false);
		setIconImage(new ImageIcon("src/icon/icon.png").getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("游戏 (G)");
		JMenu menu2 = new JMenu("帮助 (H)");
		menu1.setMnemonic('G');
		menu2.setMnemonic('H');
		JMenuItem[] item = new JMenuItem[6];
		item[0] = new JMenuItem("开局");
		item[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Ground ground = new Ground(getW(), getH(), getMines());// 创建雷场对象
				Controller controller = new Controller();
				new View(ground, getW(), getH(), getMines(), controller);// 视图层
			}
		});

		item[1] = new JMenuItem("初级");
		item[1].setAccelerator(KeyStroke.getKeyStroke("F3"));
		item[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();

				Ground ground = new Ground(9, 9, 10);// 创建雷场对象
				Controller controller = new Controller();
				new View(ground, 9, 9, 10, controller);// 视图层
			}
		});
		item[2] = new JMenuItem("中级");
		item[2].setAccelerator(KeyStroke.getKeyStroke("F4"));
		item[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();

				Ground ground = new Ground(16, 16, 40);// 创建雷场对象
				Controller controller = new Controller();
				new View(ground, 16, 16, 40, controller);// 视图层
			}
		});

		item[3] = new JMenuItem("高级");
		item[3].setAccelerator(KeyStroke.getKeyStroke("F5"));
		item[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();

				Ground ground = new Ground(16, 30, 99);// 创建雷场对象
				Controller controller = new Controller();
				new View(ground, 16, 30, 99, controller);// 视图层
			}
		});
		item[4] = new JMenuItem("自定义...     ");
		item[4].setAccelerator(KeyStroke.getKeyStroke("F6"));
		item[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame customWindows = new JFrame();

				customWindows.setLayout(new FlowLayout());
				customWindows.setIconImage(new ImageIcon("src/icon/icon.png").getImage());
				customWindows.setTitle("自定义");
				customWindows.add(new JLabel("长度："));
				final JTextField lengthTextArea = new JTextField(10);
				customWindows.add(lengthTextArea);
				customWindows.add(new JLabel("宽度："));
				final JTextField widthTextArea = new JTextField(10);
				customWindows.add(widthTextArea);
				customWindows.add(new JLabel("雷数："));
				final JTextField mineTextArea = new JTextField(10);
				customWindows.add(mineTextArea);
				JButton enterButton = new JButton("确定");
				enterButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						// TODO 自动生成的方法存根
						dispose();

						Ground ground = new Ground(Integer.parseInt(lengthTextArea.getText()), Integer.parseInt(widthTextArea.getText()), Integer.parseInt(mineTextArea.getText()));// 创建雷场对象
						Controller controller = new Controller();
						new View(ground, Integer.parseInt(lengthTextArea.getText()), Integer.parseInt(widthTextArea.getText()), Integer.parseInt(mineTextArea.getText()), controller);// 视图层
					}
				});
				customWindows.add(enterButton);
				customWindows.setTitle("自定义");
				
				customWindows.setBounds(300, 200, 200, 150);
				customWindows.setResizable(false);
				customWindows.setVisible(true);
			}
		});
		item[5] = new JMenuItem("退出");
		item[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		item[0].setAccelerator(KeyStroke.getKeyStroke("F2"));
		menu1.add(item[0]);
		menu1.addSeparator();
		for (int i = 1; i < 5; i++) {
			menu1.add(item[i]);
		}
		menu1.addSeparator();
		menu1.add(item[5]);
		menuBar.add(menu1);
		menuBar.add(menu2);

		JMenuItem sourceCode = new JMenuItem("Source Code");
		sourceCode.setAccelerator(KeyStroke.getKeyStroke("F12"));
		sourceCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime()
							.exec("rundll32 url.dll,FileProtocolHandler https://github.com/zbqhc/javaMineGame");
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		});
		

		JMenuItem aboutItem = new JMenuItem("关于 JavaMineSweeper");
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame aboutWindows = new JFrame();
				aboutWindows.setLayout(new FlowLayout());
				aboutWindows.setTitle("关于 JavaMineSweeper");
				aboutWindows.setIconImage(new ImageIcon("src/icon/icon.png").getImage());
				aboutWindows.setBounds(300, 300, 500, 400);
				aboutWindows.add(new JLabel("作者：Explorer"));

				aboutWindows.setResizable(false);
				aboutWindows.setVisible(true);
				JButton enterButton = new JButton("确定");
				enterButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						// TODO 自动生成的方法存根
						aboutWindows.setVisible(false);
						setEnabled(true);
						setVisible(true);
					}
				});
				aboutWindows.add(enterButton);
				setEnabled(false);
			}
		});

		aboutItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
		menu2.add(aboutItem);
		menu2.add(sourceCode);
		setJMenuBar(menuBar);

		this.buttons = new JButton[getW() + 2][getH() + 2];
		for (int y = 1; y <= getH(); y++) {
			for (int x = 1; x <= getW(); x++) {
				buttons[x][y] = new JButton();
				buttons[x][y].setName(String.valueOf(x) + "|"
						+ String.valueOf(y) + "|0");// 初始化label
				buttons[x][y].setBackground(Color.GRAY);// 深灰
				add(buttons[x][y]);
				buttons[x][y].addMouseListener(controller
						.createListener(ground));// 添加鼠标事件监听器
			}
		}
		setBounds(50, 100, 60 * getH(), 60 * getW());
		setVisible(true);
		
	}

	// 入口
	public static void main(String[] args) {
		final int w = 9;
		final int h = 9;
		final int counter = 10;
		Ground ground = new Ground(w, h, counter);// 创建雷场对象
		Controller controller = new Controller();
		new View(ground, w, h, counter, controller);// 视图层

	}

}
