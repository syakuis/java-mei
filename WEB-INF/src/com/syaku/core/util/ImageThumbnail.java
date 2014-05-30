/*
 * ImageThumbnail.java 2011.02.15
 *
 * Copyright (c) 2010, MEI By Seok Kyun. Choi. (최석균)
 * http://syaku.tistory.com
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */

package com.syaku.core.util;

import java.io.*;

import org.apache.log4j.Logger;
import org.apache.commons.lang.*;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import javax.imageio.ImageIO;

import java.awt.RenderingHints;

import com.syaku.core.common.AnimatedGifEncoder;
import com.syaku.core.common.GifDecoder;

public class ImageThumbnail {
  private static Logger log = Logger.getLogger(ImageThumbnail.class);

  public static void createThumbnail(String load,String save,String type,int w,int h) {

    try {
      BufferedInputStream stream_file = new BufferedInputStream(new FileInputStream(load));
      createThumbnail(stream_file,save,type,w,h);
    } catch (Exception e) {
      log.error(e);
    }

  }

  public static void createThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h) {
    createThumbnail(stream_file,save,type,w,h,true);
  }


  public static void createThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h, boolean jai) {
    try {

    if (StringUtils.equals(StringUtils.lowerCase(type),"gif")) {
      getGifImageThumbnail(stream_file,save,type,w,h);
    } else {

      if (jai) {
        getImageThumbnail(stream_file,save,type,w,h);
      } else {
        getImageIoThumbnail(stream_file,save,type,w,h);
      }

    }

    } catch (Exception e) {
      log.error(e);
    }

  }

  public static void getImageIoThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h) {

    try {
    File destFile = new File(save);
    Image info = ImageIO.read(stream_file);

    int width = info.getWidth(null);
    int height = info.getHeight(null);
    if (w < width) { width = w; }
    if (h < height) { height = h; }

    // SCALE_AREA_AVERAGING, SCALE_DEFAULT, SCALE_FAST, SCALE_REPLICATE, SCALE_SMOOTH
    Image image = info.getScaledInstance(width, height, Image.SCALE_FAST);

    int pixels[] = new int[width * height];

    PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
    pg.grabPixels();

    BufferedImage destImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    destImg.setRGB(0, 0, width, height, pixels, 0, width);
    ImageIO.write(destImg, type , destFile);

    } catch (Exception e) {
      log.error(e);
    }

  }

  public static void getImageThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h) {

    try {
      File  file = new File(save);
      BufferedImage bi = ImageIO.read(stream_file);

      int width = bi.getWidth();
      int height = bi.getHeight();
      if (w < width) { width = w; }
      if (h < height) { height = h; }

      Image atemp = bi.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);

      BufferedImage bufferIm = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D  g2 = bufferIm.createGraphics();
      //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(atemp, 0, 0, width, height, null);
      g2.dispose();

      ImageIO.write(bufferIm, type, file);
    } catch (Exception e) {
      log.error(e);
    }

  }

  public static void getGifImageThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h) {

      GifDecoder dec = new GifDecoder();
      AnimatedGifEncoder enc = new AnimatedGifEncoder();
      dec.read(stream_file);

      int cnt = dec.getFrameCount();

      int delay = 0;
      int width = 0;
      int height = 0;

      try{
        enc.start(save);
        enc.setRepeat(0);

        for (int i = 0; i < cnt; i++) {
        BufferedImage frame = dec.getFrame(i);
        delay = dec.getDelay(i);

        width = frame.getWidth();
        height = frame.getHeight();
        if (w < width) { width = w; }
        if (h < height) { height = h; }

        BufferedImage destimg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = destimg.createGraphics();

        g.drawImage(frame, 0, 0, width, height, null);
        enc.setDelay(delay);

        enc.addFrame(destimg);
        }

        enc.finish();
      }catch(Exception ex){
        log.error(ex);
      }

  }

}