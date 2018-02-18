import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Random;
import java.util.Date;

public class colorful{
	private static int width = 2048;
	private static int height = 2048;

	public static int progress = 0;

	private static Random rand = new Random(new Date().getTime());

	private static String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private static int hr = 0,hg = 0,hb = 0;

	public static void main(String[] args){
		if(args.length == 3){
			hr = check(Integer.parseInt(args[0]), 0, 255);
			hg = check(Integer.parseInt(args[1]), 0, 255);
			hb = check(Integer.parseInt(args[2]), 0, 255);
		}
		log("It's just begun!");
		create();
	}

	public static String randomString(int length){
		String rStr = "";
		for (int h = 0; h <= length; ++h) {
			int v = random(string.length() - 1);
			rStr += string.substring(v, v+1);
		}
		return rStr;
	}

	//0〜mまでの乱数を返します。
	public static int random(int m){
		int s = rand.nextInt() % (m + 1);
		int n = s < 0 ? -s : s;
		return n;
	}

	public static void log(Object o){
		System.out.println(o);
	}

	public static void progressBar(int a){
		if(a % 5 == 0) ++progress;

		String s = "";
		int l = 0;
		while(l < 100){
			++l;
			s += l < progress ? "=" : "";
		}
		s += ">";
		System.out.print("" + s + "" + String.format("%1$10s:", Integer.toString(a) + "%") + "\r");
	}

	public static void create(){
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] clr = new int[256*256*256+1];
		for(int l = 0; l <= 256*256*256; ++l){
			clr[l] = 0;
		}

		int[] pixels = new int[width * height];
		for (int q = 0; q < width; q++) {
			for (int w = 0; w < height; w++) {
				pixels[width * q + w] = rgb(255,255,255);
			}
		}

		int f = 0;

		for (int t = 0; t < 20000; ++t) {
			randomWaterBalloon(random(width - 1), random(height - 1), width, height, pixels, rgb(random(255 - hr) + hr, random(255 - hg) + hg, random(255 - hb) + hb), random(40000), 1000);
			if(t % 200 == 0) progressBar(t / 200);
		}

		progressBar(100);
		log("Creating image...");


		img.setRGB(0, 0, width, height, pixels, 0, width);
		try{
			ImageIO.write(img, "png", new File("./colorful-"+randomString(10)+".png"));
		}catch(Exception e){

		}
	}

    public static int rgb(int r,int g,int b){
        return 0xff000000 | r <<16 | g <<8 | b;
    }

    public static int r(int rgb){
    	return rgb >> 16 & 0xFF;
    }
    public static int g(int rgb){
    	return rgb >> 8 & 0xFF;
    }
    public static int b(int rgb){
    	return rgb & 0xFF;
    }

    /*
     * @param
     * x, y : センター座標
     * width, height : 画像の幅、高さ
     * ink : インクの量 (1インクで1pxの範囲が塗れます)
     * pixels : 画素配列(シャローコピーなのでそのまま使える)
     * color : sRGBで表されたカラー
     * speed : 時速(速ければ速いほど周りによく飛び散ります。) ※計算式がわからないので未実装
     *
     * @void
    */
    public static void randomWaterBalloon(int x, int y, int width, int height, int[] pixels, int color, int ink, int speed){

    	int radius = mySqrt(ink);
    	int rr = radius * radius;
    	int sx = x, sy = y;
    	int i = ink; 
    	int r = 0,g = 0,b = 0;
    	while(i > 0){
    		sx = check(x + random(radius * 2) - radius, 0, width - 1);
    		sy = check(y + random(radius * 2) - radius, 0, height - 1);

    		if((sx - x) * (sx - x) + (sy - y) * (sy - y) > rr) continue;

    		r = check(r(color) + random(40) - 20, 0, 255);
    		g = check(g(color) + random(40) - 20, 0, 255);
    		b = check(b(color) + random(40) - 20, 0, 255);
    		pixels[width * sx + sy] = rgb(r,g,b);
    		--i;
    	}
    }

    public static int check(int a, int min, int max){
    	if(a < min) a = min;
    	if(a > max) a = max;
    	return a;
    }

    /*
     * nの平方根を整数で返します。精度はないに等しいです。
     */
    public static int mySqrt(int n){
    	double a = n;
    	while(a * a > n){
    		a = myRound(a / 1.05) == a ? a - 1 : myRound(a / 1.05);
    	}
    	return (int) a;
    }

    public static int myRound(double a){
    	return (int) (a + 0.5);
    }
}