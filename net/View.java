package net;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.EOFException;
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
import javax.swing.JPanel;
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
	int level = 1;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
		// System.out.println(blockCounter);
	}

	// 构造方法
	public View(Ground ground, int w, int h, int mines, int level,
			Controller controller) {
		// TODO 自动生成的构造函数存根
		this.W = w;
		this.H = h;
		this.mines = mines;
		this.level = level;
		this.setBlockCounter(w * h);
		this.setController(controller);
		controller.setView(this);

		init(ground);
	}

	// 胜利消息框
	void victoryMsg() {

		if ((this.blockCounter == this.mines && this.VICTORY == 0)
				|| (controller.getMineFlag() == this.mines)) {

			this.VICTORY = -1;
			JOptionPane.showMessageDialog(
					null,
					"扫雷成功\n耗时："
							+ Math.abs((controller.getTimeFlag() - System
									.currentTimeMillis()) / 1000.0) + "秒\n\n",
					"恭喜", JOptionPane.INFORMATION_MESSAGE);
			dispose();
			Ground ground = new Ground(getW(), getH(), getMines());// 创建雷场对象
			Controller controller = new Controller();
			new View(ground, getW(), getH(), getMines(), this.level, controller);

		}
	}

	// 数字染色
	static void setNumColor(JButton btn) {
		if (btn.getText().equals("1")) {
			// btn.setForeground(new Color(0x0000ff));
			btn.setIcon(new ImageIcon("src/icon/1.png"));
			btn.setText("");
		}
		if (btn.getText().equals("2")) {
			// btn.setForeground(new Color(0x008000));
			btn.setIcon(new ImageIcon("src/icon/2.png"));
			btn.setText("");
		}
		if (btn.getText().equals("3")) {
			// btn.setForeground(new Color(0xff0000));
			btn.setIcon(new ImageIcon("src/icon/3.png"));
			btn.setText("");
		}
		if (btn.getText().equals("4")) {
			// btn.setForeground(new Color(0x000080));
			btn.setIcon(new ImageIcon("src/icon/4.png"));
			btn.setText("");
		}
		if (btn.getText().equals("5")) {
			// btn.setForeground(new Color(0x800000));
			btn.setIcon(new ImageIcon("src/icon/5.png"));
			btn.setText("");
		}
		if (btn.getText().equals("6")) {
			// btn.setForeground(new Color(0x008080));
			btn.setIcon(new ImageIcon("src/icon/6.png"));
			btn.setText("");
		}
		if (btn.getText().equals("7")) {
			// btn.setForeground(new Color(0x000000));
			btn.setIcon(new ImageIcon("src/icon/7.png"));
			btn.setText("");
		}
		if (btn.getText().equals("8")) {
			// btn.setForeground(new Color(0x808080));
			btn.setIcon(new ImageIcon("src/icon/8.png"));
			btn.setText("");
		}
		btn.setFont(new Font("黑体", Font.BOLD, 24));

	}

	static int collAttr(JButton btn, int key) {
		if (btn == null) {
			// System.out.println("collAttr方法:重载2\t" + "参数NULL");
			return 0;
		}
		String item = btn.getName().split("\\|")[key];
		if (item.equals("")) {
			// System.out.println("collAttr方法:重载2\t" + "获取为空");
			return 0;
		} else {
			return Integer.parseInt(item);
		}
	}

	static void collAttr(JButton btn, int key, int value) {
		btn.setName(collAttr(btn, 0) + "|" + collAttr(btn, 1) + "|"
				+ String.valueOf(value));
		// System.out.println("collAttr方法:重载3\t" + collAttr(btn, 0) + "|"
		// + collAttr(btn, 1) + "|" + String.valueOf(value));
		return;
	}

	// 开启连续空白块
	int blankBlock(JButton[][] buttons, int x, int y) {
		int counter = 0;
		if (x >= 1 && x <= W && y >= 1 && y <= H && gr[x][y] == 0
				&& collAttr(buttons[x][y], 2) == 0) {
			collAttr(buttons[x][y], 2, -3);
			setBlockCounter();
			buttons[x][y].setText("");
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
		JPanel minefield = new JPanel(new BorderLayout());
		JPanel timerfield = new JPanel(new BorderLayout());

		timerfield.setLayout(new GridLayout(1, 3, 100, 100));

		timerfield.add(new JLabel("测试"));
		timerfield.add(new JButton("测试"));
		timerfield.add(new JButton("测试"));
		// this.add(timerfield,BorderLayout.NORTH);

		minefield.setLayout(new GridLayout(getW(), getH(), 1, 1));
		this.add(minefield, BorderLayout.CENTER);
		setTitle("JavaMineSweeper");
		setResizable(false);
		setIconImage(new ImageIcon("./src/icon/icon.png").getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("游戏 (G)");
		JMenu menu2 = new JMenu("帮助 (H)");
		menu1.setMnemonic('G');
		menu2.setMnemonic('H');
		final JMenuItem[] item = new JMenuItem[7];
		item[0] = new JMenuItem("      开局");
		item[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Ground ground = new Ground(getW(), getH(), getMines());// 创建雷场对象
				Controller controller = new Controller();
				new View(ground, getW(), getH(), getMines(), getLevel(),
						controller);// 视图层
			}
		});
		if (level == 1) {
			item[1] = new JMenuItem("✓   初级");
		} else {
			item[1] = new JMenuItem("      初级");
		}
		item[1].setAccelerator(KeyStroke.getKeyStroke("F3"));
		item[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Ground ground = new Ground(9, 9, 10);// 创建雷场对象
				Controller controller = new Controller();
				new View(ground, 9, 9, 10, 1, controller);// 视图层
			}
		});
		if (level == 2) {
			item[2] = new JMenuItem("✓   中级");
		} else {
			item[2] = new JMenuItem("      中级");
		}

		item[2].setAccelerator(KeyStroke.getKeyStroke("F4"));
		item[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();

				Ground ground = new Ground(16, 16, 40);// 创建雷场对象
				new View(ground, 16, 16, 40, 2, new Controller());// 视图层
			}
		});
		if (level == 3) {
			item[3] = new JMenuItem("✓   高级");
		} else {
			item[3] = new JMenuItem("      高级");
		}

		item[3].setAccelerator(KeyStroke.getKeyStroke("F5"));
		item[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Ground ground = new Ground(22, 22, 99);// 创建雷场对象
				Controller controller = new Controller();
				new View(ground, 22, 22, 99, 3, controller);// 视图层
			}
		});
		if (level == 4) {
			item[4] = new JMenuItem("✓   自定义...     ");
		} else {
			item[4] = new JMenuItem("      自定义...     ");
		}

		item[4].setAccelerator(KeyStroke.getKeyStroke("F6"));
		item[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame customWindows = new JFrame();

				customWindows.setLayout(new FlowLayout());

				customWindows.setIconImage(new ImageIcon("src/icon/icon.png")
						.getImage());
				customWindows.setTitle("自定义");
				setEnabled(false);
				customWindows.add(new JLabel("宽度："));
				final JTextField lengthTextArea = new JTextField(12);
				lengthTextArea.addKeyListener(new KeyListener() {

					public void keyTyped(KeyEvent e) {
						// TODO 自动生成的方法存根
						int keyChar = e.getKeyChar();
						int counter = 0;
						if (keyChar >= KeyEvent.VK_0
								&& keyChar <= KeyEvent.VK_9 && counter < 5) {
							counter++;
//							System.out.println("hahhahha");
						} else {
							e.consume(); // 关键，屏蔽掉非法输入
						}
					}

					public void keyReleased(KeyEvent e) {
						// TODO 自动生成的方法存根

					}

					public void keyPressed(KeyEvent e) {
						// TODO 自动生成的方法存根

					}
				});
				customWindows.add(lengthTextArea);
				// customWindows.add(new JLabel("宽度："));
//				final JTextField widthTextArea = new JTextField(12);
//				customWindows.add(widthTextArea);
				customWindows.add(new JLabel("雷数："));
				final JTextField mineTextArea = new JTextField(12);
				mineTextArea.addKeyListener(new KeyListener() {

					public void keyTyped(KeyEvent e) {
						// TODO 自动生成的方法存根
						int keyChar = e.getKeyChar();
						int counter = 0;
						if (keyChar >= KeyEvent.VK_0
								&& keyChar <= KeyEvent.VK_9 && counter < 5) {
							counter++;
							System.out.println("hahhahha");
						} else {
							e.consume(); // 关键，屏蔽掉非法输入
						}
					}

					public void keyReleased(KeyEvent e) {
						// TODO 自动生成的方法存根

					}

					public void keyPressed(KeyEvent e) {
						// TODO 自动生成的方法存根

					}
				});
				customWindows.add(mineTextArea);
				final JButton enterButton = new JButton("确定");

				enterButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						// TODO 自动生成的方法存根

						if (lengthTextArea.getText().equals("")
								|| mineTextArea.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "请正确填写信息……",
									"皮皮虾我们走", JOptionPane.ERROR_MESSAGE);
						} else {
							dispose();
							item[1].setText("      初级");
							item[2].setText("      中级");
							item[3].setText("      高级");
							item[4].setText("✓   自定义...     ");
							customWindows.setVisible(false);
							Ground ground = new Ground(Integer
									.parseInt(lengthTextArea.getText()),
									Integer.parseInt(lengthTextArea.getText()),
									Integer.parseInt(mineTextArea.getText()));// 创建雷场对象
							Controller controller = new Controller();
							new View(ground, Integer.parseInt(lengthTextArea
									.getText()), Integer
									.parseInt(lengthTextArea.getText()),
									Integer.parseInt(mineTextArea.getText()),
									4, controller);// 视图层
						}
					}

				});
				customWindows.addWindowListener(new WindowListener() {

					public void windowOpened(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowIconified(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowDeiconified(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowDeactivated(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowClosing(WindowEvent e) {
						// TODO 自动生成的方法存根
						System.out.println("close");
						setEnabled(true);
						setVisible(true);
					}

					public void windowClosed(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowActivated(WindowEvent e) {
						// TODO 自动生成的方法存根

					}
				});
				customWindows.add(enterButton);
				customWindows.setTitle("自定义");

				customWindows.setBounds(getX()
						+ ((getY() - getX()) / 2 + 230 / 2), getY() + 30, 230,
						150);
				customWindows.setResizable(false);
				customWindows.setVisible(true);
			}
		});
		item[5] = new JMenuItem("      扫雷英雄榜...");
		item[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame customWindows = new JFrame();

				customWindows.setLayout(new FlowLayout());
				customWindows.setIconImage(new ImageIcon("src/icon/icon.png")
						.getImage());
				customWindows.setTitle("扫雷英雄榜");
				setEnabled(false);
				customWindows.add(new JLabel("长度："));
				final JTextField lengthTextArea = new JTextField(12);
				lengthTextArea.addKeyListener(new KeyListener() {

					public void keyTyped(KeyEvent e) {
						// TODO 自动生成的方法存根
						int keyChar = e.getKeyChar();
						int counter = 0;
						if (keyChar >= KeyEvent.VK_0
								&& keyChar <= KeyEvent.VK_9 && counter < 5) {
							counter++;
							System.out.println("hahhahha");
						} else {
							e.consume(); // 关键，屏蔽掉非法输入
						}
					}

					public void keyReleased(KeyEvent e) {
						// TODO 自动生成的方法存根

					}

					public void keyPressed(KeyEvent e) {
						// TODO 自动生成的方法存根

					}
				});
				customWindows.add(lengthTextArea);
				customWindows.add(new JLabel("宽度："));
				final JTextField widthTextArea = new JTextField(12);
				customWindows.add(widthTextArea);
				customWindows.add(new JLabel("雷数："));
				final JTextField mineTextArea = new JTextField(12);
				customWindows.add(mineTextArea);
				JButton enterButton = new JButton("确定");

				enterButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						// TODO 自动生成的方法存根

						if (lengthTextArea.getText().equals("")
								|| widthTextArea.equals("")
								|| mineTextArea.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "请正确填写信息……",
									"皮皮虾我们走", JOptionPane.ERROR_MESSAGE);
						} else {
							dispose();
							Ground ground = new Ground(Integer
									.parseInt(lengthTextArea.getText()),
									Integer.parseInt(widthTextArea.getText()),
									Integer.parseInt(mineTextArea.getText()));// 创建雷场对象
							Controller controller = new Controller();
						}
					}

				});
				customWindows.addWindowListener(new WindowListener() {

					public void windowOpened(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowIconified(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowDeiconified(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowDeactivated(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowClosing(WindowEvent e) {
						// TODO 自动生成的方法存根
						System.out.println("close");
						setEnabled(true);
						setVisible(true);
					}

					public void windowClosed(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowActivated(WindowEvent e) {
						// TODO 自动生成的方法存根

					}
				});
				customWindows.add(enterButton);
				customWindows.setTitle("自定义");
				customWindows.setBounds(getX()
						+ ((getY() - getX()) / 2 + 230 / 2), getY() + 30, 230,
						150);

				customWindows.setResizable(false);
				customWindows.setVisible(true);
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
		menu1.addSeparator();
		item[6] = new JMenuItem("      退出");
		item[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu1.add(item[6]);
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
				aboutWindows.setIconImage(new ImageIcon("src/icon/icon.png")
						.getImage());
				aboutWindows.setBounds(getX()
						+ ((getY() - getX()) / 2 + 250 / 2), getY() + 30, 320,
						100);
				aboutWindows.add(new JLabel("作者：张博强 李昕 王妍"));
				aboutWindows.addWindowListener(new WindowListener() {

					public void windowOpened(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowIconified(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowDeiconified(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowDeactivated(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowClosing(WindowEvent e) {
						// TODO 自动生成的方法存根
						System.out.println("close");
						setEnabled(true);
						setVisible(true);
					}

					public void windowClosed(WindowEvent e) {
						// TODO 自动生成的方法存根

					}

					public void windowActivated(WindowEvent e) {
						// TODO 自动生成的方法存根

					}
				});
				aboutWindows.setResizable(false);
				aboutWindows.setVisible(true);
				JButton enterButton = new JButton("确定");
				enterButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						// TODO 自动生成的方法存根
						aboutWindows.dispose();
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
				minefield.add(buttons[x][y]);
				buttons[x][y].addMouseListener(controller
						.createListener(ground));// 添加鼠标事件监听器
			}
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 获取屏幕的尺寸

		int screenWidth = screenSize.width; // 获取屏幕的宽

		int screenHeight = screenSize.height;
		setBounds((screenWidth - 40 * getH()) / 2,
				(screenHeight - 40 * getW()) / 2, 40 * getH(), 40 * getW());
		setVisible(true);

	}

	// 入口
	public static void main(String[] args) {
		final int w = 9;
		final int h = 9;
		final int counter = 10;
		new View(new Ground(w, h, counter), w, h, counter, 1, new Controller());// 视图层

	}

}
