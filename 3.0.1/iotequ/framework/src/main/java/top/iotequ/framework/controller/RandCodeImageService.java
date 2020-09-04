package top.iotequ.framework.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShadowFilter;
import com.jhlabs.image.TransformFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.util.Util;


@RestController
public class RandCodeImageService {

	private static final String SESSION_KEY_OF_RAND_CODE = "randCode"; // todo 要统一常量

	private static final String MOBILE_CHARSET = "0123456789";
	private static final String PC_CHARSET = "0123456789aAcCdeEfFhHikKmMrRtTvVwWxX";

	private static final int count = 200;

	private static final int width = 105;

	private static final int height = 35;

	/**
	 * 干扰线的长度=1.414*lineWidth
	 */
	private static final int lineWidth = 4;

	private String generateStr(String sourseStr,int codeLength) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < codeLength; i++) {
			sb.append(sourseStr.charAt(random.nextInt(sourseStr.length())));
		}
		return sb.toString();
	}

	@RequestMapping("/res/randCodeImage")
	public void doGet(String session,final HttpServletRequest request, final HttpServletResponse response)
			throws  IOException {
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// response.setContentType("image/png");

		// 在内存中创建图象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		final Graphics2D graphics = (Graphics2D) image.getGraphics();

		// 设定背景颜色
		graphics.setColor(Color.WHITE); // ---1
		graphics.fillRect(0, 0, width, height);
		// 设定边框颜色
		// graphics.setColor(getRandColor(100, 200)); // ---2
		graphics.drawRect(0, 0, width - 1, height - 1);

		final Random random = new Random();
		for (int i = 0; i < count; i++) {
			graphics.setColor(getRandColor(64, 255)); // ---3
			final int x = random.nextInt(width - lineWidth - 1) + 1; // 保证画在边框之内
			final int y = random.nextInt(height - lineWidth - 1) + 1;
			final int xl = random.nextInt(lineWidth);
			final int yl = random.nextInt(lineWidth);
			graphics.drawLine(x, y, x + xl, y + yl);
		}

		final String resultCode = generateStr(Util.isMobile(request) ? MOBILE_CHARSET : PC_CHARSET,4);
		for (int i = 0; i < resultCode.length(); i++) {
			graphics.setColor(getRandColor(0, 64));
			graphics.setFont(new Font("Times New Roman", Font.BOLD, 24));
			graphics.drawString(String.valueOf(resultCode.charAt(i)), (23 * i) + 16, 24+random.nextInt(10));
		}

		// 将认证码存入SESSION
		Util.setSessionAttribute(SESSION_KEY_OF_RAND_CODE, resultCode);
		//System.out.println(resultCode);
		shadow(graphics,image);
		graphics.dispose();
		fish(image);
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}
	public static String getSessionRandCode() {
		String vc=StringUtil.toString(Util.getSessionAttribute(SESSION_KEY_OF_RAND_CODE));
		return vc;
	}

	private Color getRandColor(int fc, int bc) { // 取得给定范围随机颜色
		final Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}

		final int r = fc + random.nextInt(bc - fc);
		final int g = fc + random.nextInt(bc - fc);
		final int b = fc + random.nextInt(bc - fc);

		return new Color(r, g, b);
	}

	public void shadow(Graphics2D graph,BufferedImage image)
	{
		ShadowFilter shadowFilter = new ShadowFilter();
		shadowFilter.setRadius(10);
		shadowFilter.setDistance(5);
		shadowFilter.setOpacity(0.5f);

		Random rand = new Random();

		RippleFilter rippleFilter = new RippleFilter();
		rippleFilter.setWaveType(RippleFilter.SINE);
		rippleFilter.setXAmplitude(7.6f);
		rippleFilter.setYAmplitude(rand.nextFloat() + 1.0f);
		rippleFilter.setXWavelength(rand.nextInt(7) + 8);
		rippleFilter.setYWavelength(rand.nextInt(3) + 2);
		rippleFilter.setEdgeAction(TransformFilter.BILINEAR);

		BufferedImage effectImage = rippleFilter.filter(image, null);
		effectImage = shadowFilter.filter(effectImage, null);

		graph.drawImage(effectImage, 0, 0, null, null);

	}

	public  void fish(BufferedImage image)
	{

		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		int pix[] = new int[imageHeight * imageWidth];
		int j = 0;

		for (int j1 = 0; j1 < imageWidth; j1++)
		{
			for (int k1 = 0; k1 < imageHeight; k1++)
			{
				pix[j] = image.getRGB(j1, k1);
				j++;
			}

		}

		double distance = ranInt(imageWidth / 16, imageWidth / 10);

		// put the distortion in the (dead) middle
		int widthMiddle = image.getWidth() / 2;
		int heightMiddle = image.getHeight() / 2;

		// again iterate over all pixels..
		for (int x = 0; x < image.getWidth(); x++)
		{
			for (int y = 0; y < image.getHeight(); y++)
			{

				int relX = x - widthMiddle;
				int relY = y - heightMiddle;

				double d1 = Math.sqrt(relX * relX + relY * relY);
				if (d1 < distance)
				{

					int j2 = widthMiddle
							+ (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (double) (x - widthMiddle));
					int k2 = heightMiddle
							+ (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (double) (y - heightMiddle));
					image.setRGB(x, y, pix[j2 * imageHeight + k2]);
				}
			}

		}

	}

	private  int ranInt(int i, int j)
	{
		double d = Math.random();
		return (int) ((double) i + (double) ((j - i) + 1) * d);
	}

	/**
	 * implementation of: g(s) = - (3/4)s3 + (3/2)s2 + (1/4)s, with s from 0 to
	 * 1
	 *
	 * @param s
	 * @return
	 */
	private double fishEyeFormula(double s)
	{
		if (s < 0.0D)
			return 0.0D;
		if (s > 1.0D)
			return s;
		else
			return -0.75D * s * s * s + 1.5D * s * s + 0.25D * s;
	}

}
