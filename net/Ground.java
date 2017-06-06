package net;

import java.util.Random;

public class Ground {
	private int mine;
	private int w;
	private int h;
	private int ground[][];

	public Ground(int w, int h, int mine) {
		this.ground = new int[w + 2][h + 2];
		this.mine = mine;
		this.w = w;
		this.h = h;
		for (int y = 0; y < h + 2; y++) {
			for (int x = 0; x < w + 2; x++) {
				ground[x][y] = 0;
			}
		}
	}

	//生成只包括地雷的地图
	public int[][] initMinefield(int x, int y) {
		System.out.println(x);
		System.out.println(y);
		Random rander = new Random();
		int tx, ty;
		for (int i = 0; i < mine; i++) {
			tx = rander.nextInt(w) + 1;
			ty = rander.nextInt(h) + 1;
			if (ground[tx][ty] == -1 || (tx == x && ty == y)) {
				i--;
			} else {
				ground[tx][ty] = -1;
			}
		}
		this.calcMine();
		return ground;
	}

	//生成包括提示数字的地图
	private void calcMine() {
		int ground[][] = new int[w + 2][h + 2];
		for (int y = 0; y < h + 2; y++) {
			for (int x = 0; x < w + 2; x++) {
				ground[x][y] = 0;
			}
		}
		for (int y = 1; y <= h; y++) {
			for (int x = 1; x <= w; x++) {
				if (this.ground[x][y] != -1) {
					ground[x][y] = -1*(this.ground[x - 1][y - 1]
							+ this.ground[x][y - 1] + this.ground[x + 1][y - 1]
							+ this.ground[x - 1][y] + this.ground[x + 1][y]
							+ this.ground[x - 1][y + 1] + this.ground[x][y + 1]
							+ this.ground[x + 1][y + 1]);
				} else {
					ground[x][y]=-1;
				}
				if (ground[x][y]==-1){
					System.out.print("● ");
				}else if (ground[x][y]==0){
					System.out.print("  ");
				}else{
				System.out.print(ground[x][y] + " ");
				// System.out.println(x+"   "+y);
				}
			}
			System.out.println("");
		}
		for (int y = 1; y <= h; y++) {
			for (int x = 1; x <= w; x++) {
				this.ground[x][y]=ground[x][y];
				}
			
		}
	}

	public int getMine() {
		return mine;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	//测试类
	public static void main(String[] args) {
		Ground ground = new Ground(16, 16, 40);
		int[][] gr = ground.initMinefield(5, 5);
		System.out.println("");
		for (int y = 1; y <= ground.getH(); y++) {
			for (int x = 1; x <= ground.getW(); x++) {
				System.out.print(gr[x][y]+" ");
				}
			System.out.println("");
		}
	}
}
