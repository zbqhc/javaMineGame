package net;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument.Iterator;

public class Controller {

	private TreeSet<BlockClass> blockset = new TreeSet<BlockClass>();

	private View view;
	private int mineFlag=0;
	private long timeFlag=0;
	public long getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(long timeFlag) {
		this.timeFlag = timeFlag;
	}

	public int getMineFlag() {
		return mineFlag;
	}

	public void setMineFlag(int mineFlag) {
		this.mineFlag = mineFlag;
	}

	int[] mouseFlag = new int[4];
	int mouseDownClock=0;

	Timer timer = new Timer();

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	void quickDeal(JButton btn) {
		if (view.gr[View.collAttr(btn, 0)][View.collAttr(btn, 1)] > 0
				&& View.collAttr(btn, 2) != 0) {// �������
			if (quickBlock(view.buttons, View.collAttr(btn, 0),
					View.collAttr(btn, 1)).first().getX() == view.gr[View
					.collAttr(btn, 0)][View.collAttr(btn, 1)]) {
				System.out.println("����");
				for (BlockClass it : quickBlock(view.buttons,
						View.collAttr(btn, 0), View.collAttr(btn, 1))) {
					System.out.println("quickBlock: x=" + it.getX() + "\ty="
							+ it.getY());
					if (it.getY() < 0) {
						continue;// ////////////////////////////////////////////
					} else {
						try {
							clickBlock(view.buttons, it.getX(), it.getY());
						} catch (IOException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}

					}

				}
			}

		}
	}

	void clickBlock(JButton[][] btns, int x, int y) throws IOException {
		if (getTimeFlag()==0){
			setTimeFlag(System.currentTimeMillis());
			System.out.println(timeFlag);
		}
		if (view.gr[x][y] == -1 && View.collAttr(btns[x][y], 2) == 0) {// ���е���
			btns[x][y].setIcon(new ImageIcon("src/icon/landmine.png"));
			btns[x][y].setText("");
			btns[x][y].setBackground(Color.RED);
			JOptionPane.showMessageDialog(null, "ף�´κ���", "ɨ��ʧ��",
					JOptionPane.WARNING_MESSAGE);
			view.dispose();
			Ground ground = new Ground(view.getW(), view.getH(), view.getMines());// �����׳�����
			Controller controller = new Controller();
			new View(ground, view.getW(), view.getH(), view.getMines(),view.level, controller);
		} else if (view.gr[x][y] == 0 && View.collAttr(btns[x][y], 2) == 0) {// ���пհ׿�
			view.blankBlock(view.buttons, x, y);
		} else if (View.collAttr(btns[x][y], 2) == 0) {// �������ֿ���δ�㿪��
			btns[x][y].setText(String.valueOf(view.gr[x][y]));
			btns[x][y].setBackground(Color.LIGHT_GRAY);// ��ɫ���Ϊǳ��
			view.setBlockCounter();// ���¼�����
			View.setNumColor(btns[x][y]);
			View.collAttr(btns[x][y], 2, view.gr[x][y]);// ��2λ����޸�Ϊ��Ӧ������
		}
		view.victoryMsg();
	}

	MouseListener createListener(final Ground ground) {
		MouseListener MListener;
		MListener = new MouseListener() {

			

			public void mouseReleased(final MouseEvent e) {
				// TODO �Զ����ɵķ������
				if (e.getClickCount()>=2){
					try {
						quickDeal((JButton) e.getSource());
					} catch (Exception e2) {
						// TODO: handle exception
						System.out.println("������·����");
					}
				}
			}

			public void mousePressed(MouseEvent e) {
				// TODO �Զ����ɵķ������

			}

			public void mouseExited(MouseEvent e) {
				// TODO �Զ����ɵķ������

			}

			public void mouseEntered(MouseEvent e) {
				// TODO �Զ����ɵķ������

			}

			// ������굥���¼�
			public void mouseClicked(MouseEvent e) {
				JButton btn = (JButton) e.getSource();
				if (e.getButton() == 1) {// ������
					view.victoryFlag++;

					if (view.victoryFlag == 1) {// Ϊ��һ�ε��������׳���ͼ
						view.gr = ground.initMinefield(View.collAttr(btn, 0),
								View.collAttr(btn, 1));
					}
					try {
						clickBlock(view.buttons, View.collAttr(btn, 0),
								View.collAttr(btn, 1));
					} catch (IOException e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}

				} else if (e.getButton() == 3) {// ����Ҽ�
					if (View.collAttr(btn, 2) == 0) {
						//btn.setText("��");
						btn.setIcon(new ImageIcon("src/icon/redflag.png"));
						Date date = new Date(0);
						System.out.println("time"+date.getTime());
						btn.setForeground(new Color(0xff0000));
						View.collAttr(btn, 2, 1000100);
						try {
							if (view.gr[View.collAttr(btn, 0)][View.collAttr(btn, 1)]==-1){
								mineFlag++;
								System.out.println("mine counter by contraler:"+getMineFlag());
								view.victoryMsg();
							}else{
								mineFlag--;
								System.out.println("mine counter by contraler:"+getMineFlag());
								view.victoryMsg();
							}
						} catch (Exception e2) {
							// TODO: handle exception
							System.out.println("������·����");
						}
						
						btn.setFont(new Font("����", Font.PLAIN, 16));// ��ԭ����
					} else if (View.collAttr(btn, 2) == 1000100) {
//						btn.setText("?");
						btn.setIcon(new ImageIcon("src/icon/mark.png"));
						btn.setForeground(new Color(0x000000));
						try {
							if (view.gr[View.collAttr(btn, 0)][View.collAttr(btn, 1)]==-1){
								mineFlag--;
							}else{
								mineFlag++;
								System.out.println("mine counter by contraler:"+getMineFlag());
								view.victoryMsg();
							}
						} catch (Exception e2) {
							// TODO: handle exception
							System.out.println("������·����");
						}
						View.collAttr(btn, 2, -2);
//						btn.setFont(new Font("����", Font.PLAIN, 16));// ��ԭ����
					} else if (View.collAttr(btn, 2) == -2) {
						//btn.setText("");
						btn.setIcon(null);
						View.collAttr(btn, 2, 0);
//						btn.setFont(new Font("����", Font.PLAIN, 16));// ��ԭ����
					}

				} else if (e.getButton() == 4) {
					;// pass
				}

			}
		};
		return MListener;

	}

	TreeSet<BlockClass> quickBlock(JButton[][] btn, int x, int y) {

		this.blockset.clear();
		System.out.print("[[(" + x + "," + y + ")");
		blockset.add(new BlockClass((View.collAttr(btn[x - 1][y - 1], 2)
				+ View.collAttr(btn[x][y - 1], 2)
				+ View.collAttr(btn[x + 1][y - 1], 2)
				+ View.collAttr(btn[x - 1][y], 2)
				+ View.collAttr(btn[x + 1][y], 2)
				+ View.collAttr(btn[x - 1][y + 1], 2)
				+ View.collAttr(btn[x][y + 1], 2) + View.collAttr(
				btn[x + 1][y + 1], 2)) / 1000000, -1));
		if (View.collAttr(btn[x - 1][y - 1], 2) == 0) {
			this.blockset.add(new BlockClass(x - 1, y - 1));
		}
		if (View.collAttr(btn[x][y - 1], 2) == 0) {
			this.blockset.add(new BlockClass(x, y - 1));
		}
		if (View.collAttr(btn[x + 1][y - 1], 2) == 0) {
			this.blockset.add(new BlockClass(x + 1, y - 1));
		}
		if (View.collAttr(btn[x - 1][y], 2) == 0) {
			this.blockset.add(new BlockClass(x - 1, y));
		}
		if (View.collAttr(btn[x + 1][y], 2) == 0) {
			this.blockset.add(new BlockClass(x + 1, y));
		}
		if (View.collAttr(btn[x - 1][y + 1], 2) == 0) {
			this.blockset.add(new BlockClass(x - 1, y + 1));
		}
		if (View.collAttr(btn[x][y + 1], 2) == 0) {
			this.blockset.add(new BlockClass(x, y + 1));
		}
		if (View.collAttr(btn[x + 1][y + 1], 2) == 0) {
			this.blockset.add(new BlockClass(x + 1, y + 1));
		}

		return blockset;

	}

}

class BlockClass implements Comparable {
	private int x;
	private int y;

	public BlockClass(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int compareTo(Object o) {
		BlockClass bc = (BlockClass) o;
		if (y > bc.getY()) {
			return 1;
		} else {
			return -1;
		}
	}
}