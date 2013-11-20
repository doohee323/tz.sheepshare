package tz.extend.util;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tz.extend.util.ObjUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : ImageUtil
 * 설    명 : 이미지 변환 UTIL 타입 변환 (ex. jpg -> png) 크기 변환 (배율) 변환룰 : jpg->png (0) png->jpg (0)
 *        psd->jpg (0) psd->png (0) jpg->gif (0) png->gif (0)
 * 작 성 자 : TZ
 * 작성일자 : 2013.08.10
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013.08.10
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class ImageUtil {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    private final static int IMAGE_JPEG = 1;
    private final static int IMAGE_PNG = 2;
    private final static int IMAGE_GIF = 3;
    private final static int IMAGE_PSD = 4;
    private final static int IMAGE_THUMB = 5;
    private final static float IMAGE_THUMB_RATIO = 0.3F;

    public final static String FROM_FILE = "fromFile";
    public final static String TO_FILE = "toFile";
    public final static String CROP_WIDTH = "cropWidth";
    public final static String CROP_HEIGHT = "cropHeight";
    public final static String CROPX = "cropX";
    public final static String CROPY = "cropY";
    public final static String BGRX = "bgrX";
    public final static String BGRY = "bgrY";
    public final static String BGRFILE = "bgrFile";
    public final static String ADDW = "addW";
    public final static String ADDH = "addH";
    public final static String WIDTH = "width";
    public final static String HEIGHT = "height";
    public final static String RATIO = "ratio";
    public final static String COMPRESSSION = "compression";

    private int nFrom = 0;

    private int nTo = 0;

    private String fromFile = "";

    private String toFile = "";

    private String toExt = "";

    private float nRatio = 0.0F;

    private float nCompression = 0.0F;

    private int nWidth = 0;

    private int nHeight = 0;

    private int nCropX = 0;

    private int nCropY = 0;

    private int nCropWidth = 0;

    private int nCropHeight = 0;

    private int nBgrX = 0;

    private int nBgrY = 0;

    private int nAddW = 0;

    private int nAddH = 0;

    private String bgrFile = "";

    public ImageUtil() {}

    /**
     * <pre>
     *  파일 변환 Main Util
     * </pre>
     * @param inputData
     */
    public void convertImage( Map<String, Object> inputData ) {
        try {
            if(inputData.get( "fromFile" ) != null) fromFile = (String)inputData.get( FROM_FILE );
            if(inputData.get( "fromFile" ) != null) toFile = (String)inputData.get( TO_FILE );
            if(inputData.get( "cropWidth" ) != null) nCropWidth = (Integer)inputData.get( CROP_WIDTH );
            if(inputData.get( "cropHeight" ) != null) nCropHeight = (Integer)inputData.get( CROP_HEIGHT );
            if(inputData.get( "cropX" ) != null) nCropX = (Integer)inputData.get( CROPX );
            if(inputData.get( "cropY" ) != null) nCropY = (Integer)inputData.get( CROPY );

            if(inputData.get( "bgrX" ) != null) nBgrX = (Integer)inputData.get( BGRX );
            if(inputData.get( "bgrY" ) != null) nBgrY = (Integer)inputData.get( BGRY );
            if(inputData.get( "bgrFile" ) != null) bgrFile = (String)inputData.get( BGRFILE );

            if(inputData.get( "addW" ) != null) nAddW = (Integer)inputData.get( ADDW );
            if(inputData.get( "addH" ) != null) nAddH = (Integer)inputData.get( ADDH );

            if ( inputData.get( "ratio" ) != null && (Float)inputData.get( RATIO ) != 0.0 )
                nRatio = (Float)inputData.get( RATIO );

            if ( inputData.get( "compression" ) != null && (Float)inputData.get( COMPRESSSION ) != 0.0 )
                nCompression = (Float)inputData.get( COMPRESSSION );

            if(inputData.get( "width" ) != null) {
                nWidth = (Integer)inputData.get( WIDTH );
            }
            if(inputData.get( "height" ) != null) {
                nHeight = (Integer)inputData.get( HEIGHT );
            }

            String fromExt = fromFile.substring( fromFile.lastIndexOf( "." ) + 1, fromFile.length() ).toLowerCase();
            toExt = toFile.substring( toFile.lastIndexOf( "." ) + 1, toFile.length() );
            toFile = toFile.substring(0, toFile.lastIndexOf( "." ) + 1) + fromExt;

            if ( fromExt.equals( "jpg" ) )
                nFrom = IMAGE_JPEG;
            if ( toExt.equals( "jpg" ) )
                nTo = IMAGE_JPEG;

            if ( fromExt.equals( "png" ) )
                nFrom = IMAGE_PNG;
            if ( toExt.equals( "png" ) )
                nTo = IMAGE_PNG;

            if ( fromExt.equals( "gif" ) )
                nFrom = IMAGE_GIF;
            if ( toExt.equals( "gif" ) )
                nTo = IMAGE_GIF;

            if ( fromExt.equals( "psd" ) )
                nFrom = IMAGE_PSD;
            if ( toExt.equals( "psd" ) )
                nTo = IMAGE_PSD;

            if ( toExt.equals( "thumb" ) )
                nTo = IMAGE_THUMB;
            if ( toExt.equals( "ico" ) )
                nTo = IMAGE_THUMB;

            if ( nFrom == 0 || nTo == 0 ) {
                logger.debug( "Not supported type!!!" );
            } else if ( nFrom < IMAGE_PSD && nTo < IMAGE_PSD ) {
                convertImageFile();
                //            } else if ( nFrom == IMAGE_PSD ) {
                //                toFile = toFile.substring( 0, toFile.lastIndexOf( "." ) );
                //                util.convertImgFromPSDFile();
            } else if ( nTo == IMAGE_THUMB ) {
                nRatio = IMAGE_THUMB_RATIO;
                convertImageFile();
            }
        } catch ( Exception e ) {
            logger.error( "Not supported type!!!" + e );
        }
    }

    /**
     * <pre>
     *  파일 변환 Main Util
     * </pre>
     * @param inputData
     */
    public static BufferedImage convertImageStream( Map<String, Object> inputData ) {
        BufferedImage img = null;
    	try {
            ImageUtil util = new ImageUtil();
            int destWidth = 20;
            int destHeight = 20;

            if ( !ObjUtil.isNull(inputData.get("width")) ) {
         	   destWidth = Integer.parseInt(inputData.get("width").toString());
            }

            if ( !ObjUtil.isNull(inputData.get("height")) ) {
         	   destHeight = Integer.parseInt(inputData.get("height").toString());
            }

            img = util.resize((byte[])inputData.get("fileObj"), inputData.get("fileNm").toString() ,destWidth ,destHeight );

        } catch ( Exception e ) {
            logger.error( "Not supported type!!!" + e );
        }
    	return img;
    }

    /**
     * <pre>
     * Image로 부터 BufferedImage 생성 (PNG)
     * </pre>
     * @param src
     * @param aType
     * @param aWidth
     * @param aHeight
     * @param aAddW
     * @param aAddH
     * @return biRes
     * @throws IOException
     */
    public BufferedImage createBufferedPNG( BufferedImage src, int aType, int aWidth, int aHeight, int aAddW, int aAddH ) throws IOException {
        if ( aType == IMAGE_PNG && hasAlpha( src ) ) {
            aType = BufferedImage.TYPE_INT_ARGB;
        } else {
            aType = BufferedImage.TYPE_INT_RGB;
        }
        Image image = src.getScaledInstance( nWidth, nHeight, src.getType() );
        BufferedImage biRes = new BufferedImage( src.getColorModel(), src.getColorModel().createCompatibleWritableRaster( aWidth, aHeight ), src.isAlphaPremultiplied(), null );
        Graphics2D g = biRes.createGraphics();
        if ( aAddW > 0 || aAddH > 0 ) {
            g.drawImage( image, 0, 0, aWidth - aAddW, aHeight - aAddH, Color.WHITE, null );
        } else {
            g.drawImage( image, 0, 0, aWidth, aHeight, null );
        }
        return biRes;
    }

    /**
     * <pre>
     * Image로 부터 BufferedImage 생성
     * </pre>
     * @param src
     * @param aType
     * @param aWidth
     * @param aHeight
     * @param aAddW
     * @param aAddH
     * @return bi
     */
    public BufferedImage createBufferedImage( Image src, int aType, int aWidth, int aHeight, int aAddW, int aAddH ) {
        if ( aType == IMAGE_PNG && hasAlpha( src ) ) {
            aType = BufferedImage.TYPE_INT_ARGB;
        } else {
            aType = BufferedImage.TYPE_INT_RGB;
        }
        BufferedImage bi = new BufferedImage( aWidth, aHeight, aType );
        Graphics g = bi.createGraphics();
        if ( aAddW > 0 || aAddH > 0 ) {
            g.drawImage( src, 0, 0, aWidth - aAddW, aHeight - aAddH, Color.WHITE, null );
        } else {
            g.drawImage( src, 0, 0, aWidth, aHeight, Color.WHITE, null );
        }
        g.dispose();
        return bi;
    }

    /**
     * <pre>
     * convertImageFile
     * </pre>
     * @throws Exception
     */
    public void convertImageFile() throws Exception {
        // 크기 비율 계산
    	if (nWidth == 0 || nHeight == 0)
    		getImageSize( fromFile, nRatio );

        // 이미지 크기 변경
        BufferedImage source = null;
        source = resize( new File( fromFile ), nWidth, nHeight );

        // 메모리 오류 발생 위험
        if ( nTo == IMAGE_JPEG || source.getColorModel().getTransparency() != Transparency.OPAQUE ) {
            //source = fillTransparentPixels( source, Color.WHITE );
        }

        // 이미지 자르기
        source = cropImage( source );

        // background 처리
        if(!bgrFile.equals("")) {
            //if ( nBgrX != 0 || nBgrY != 0 ) {
            File fFile = new File( bgrFile );
            Image bgrImg = new ImageIcon( fFile.getCanonicalPath() ).getImage();
            source = createBufferedImage( bgrImage( bgrImg, source, nTo, nBgrX, nBgrY ), nTo, nWidth, nHeight, nAddW, nAddH );
        }
        //}

        // 생성
        if ( nTo == IMAGE_JPEG && nCompression != 0.0F) {
            saveCompressedImage( source );
        } else {
            saveImage( source );
        }
    }

    /**
     * <pre>
     * BufferedImage resize
     * </pre>
     * @param file
     * @param destWidth
     * @param destHeight
     * @return destImg
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public BufferedImage resize( File file, int destWidth, int destHeight ) throws IOException {
        Image srcImg = null;
        if ( nFrom == IMAGE_GIF || nFrom == IMAGE_PNG ) {
            srcImg = ImageIO.read( file );
        } else {
            // BMP가 아닌 경우 ImageIcon을 활용해서 Image 생성
            // 이렇게 하는 이유는 getScaledGroup를 통해 구한 이미지를
            // PixelGrabber.grabPixels로 리사이즈 할때
            // 빠르게 처리하기 위함이다.
            srcImg = new ImageIcon( file.toURL() ).getImage();
        }

        Image imgTarget = srcImg.getScaledInstance( destWidth, destHeight, Image.SCALE_DEFAULT );
        int pixels[] = new int[destWidth * destHeight];
        PixelGrabber pg = new PixelGrabber( imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth );
        try {
            pg.grabPixels();
        } catch ( InterruptedException e ) {
            throw new IOException( e.getMessage() );
        }
        BufferedImage destImg = new BufferedImage( destWidth, destHeight, BufferedImage.TYPE_INT_RGB );
        destImg.setRGB( 0, 0, destWidth, destHeight, pixels, 0, destWidth );

        return destImg;
    }

    /**
     * <pre>
     * BufferedImage resize
     * </pre>
     * @param file
     * @param destWidth
     * @param destHeight
     * @return destImg
     * @throws IOException
     */
    public BufferedImage resize( byte[] file, String fileNm, int destWidth, int destHeight ) throws IOException {

    	String fileExt = fileNm.substring( fileNm.lastIndexOf( "." ) + 1, fileNm.length() ).toLowerCase();

        if ( fileExt.equals( "jpg" ) )
            nFrom = IMAGE_JPEG;

        if ( fileExt.equals( "png" ) )
            nFrom = IMAGE_PNG;

        if ( fileExt.equals( "gif" ) )
            nFrom = IMAGE_GIF;

        if ( fileExt.equals( "psd" ) )
            nFrom = IMAGE_PSD;

        if ( toExt.equals( "thumb" ) )
            nTo = IMAGE_THUMB;

        Image srcImg = null;
        if ( nFrom == IMAGE_GIF || nFrom == IMAGE_PNG ) {
            srcImg = ImageIO.read( new ByteArrayInputStream(file) );
        } else {
            // BMP가 아닌 경우 ImageIcon을 활용해서 Image 생성
            // 이렇게 하는 이유는 getScaledGroup를 통해 구한 이미지를
            // PixelGrabber.grabPixels로 리사이즈 할때
            // 빠르게 처리하기 위함이다.
            //srcImg = new ImageIcon( file.toURL() ).getImage();
            srcImg = new ImageIcon(file).getImage();
        }

        Image imgTarget = srcImg.getScaledInstance( destWidth, destHeight, Image.SCALE_DEFAULT );
        int pixels[] = new int[destWidth * destHeight];
        PixelGrabber pg = new PixelGrabber( imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth );
        try {
            pg.grabPixels();
        } catch ( InterruptedException e ) {
            throw new IOException( e.getMessage() );
        }
        BufferedImage destImg = new BufferedImage( destWidth, destHeight, BufferedImage.TYPE_INT_RGB );
        destImg.setRGB( 0, 0, destWidth, destHeight, pixels, 0, destWidth );

        return destImg;
    }

    /**
     * <pre>
     * 이미지 CROP(자르기)
     * </pre>
     * @param src
     * @return src
     * @throws IOException
     */
    public BufferedImage cropImage( BufferedImage src ) throws IOException {
        if ( nCropX > 0 || nCropY > 0 || nCropWidth > 0 || nCropHeight > 0 ) {
            return src.getSubimage( nCropX, nCropY, nCropWidth - nCropX, nCropHeight - nCropY );
        } else {
            return src;
        }
    }

    /**
     * <pre>
     * Map getImageSize
     * </pre>
     * @param imgName
     * @return inputData
     * @throws IOException
     */
    public static Map getImageSize( String imgName ) throws IOException {
        Image image = ImageIO.read( new File( imgName ) );
        Map inputData = new HashMap();
        inputData.put( "width", image.getWidth( null ) );
        inputData.put( "height", image.getHeight( null ) );
        return inputData;
    }

    /**
     * 이미지 크기 변경
     *
     * @throws IOException
     */
    /**
     * <pre>
     * 이미지 크기 변경
     * </pre>
     * @param aFileNm
     * @param aRatio
     * @throws IOException
     */
    public void getImageSize( String aFileNm, float aRatio ) throws IOException {
        File fFile = new File( aFileNm );
//        logger.debug( "fFile===>" + fFile.getCanonicalPath() );
        Image img = new ImageIcon( fFile.getCanonicalPath() ).getImage();
        int aImgWidth = img.getWidth( null );
        int aImgHeight = img.getHeight( null );
        if ( aRatio != 0.0F ) {
            float dw = Float.parseFloat( Integer.toString( aImgWidth ) ) * aRatio;
            float dh = Float.parseFloat( Integer.toString( aImgHeight ) ) * aRatio;
            String sw = Float.toString( dw );
            sw = sw.substring( 0, sw.indexOf( "." ) );
            String sh = Float.toString( dh );
            sh = sh.substring( 0, sh.indexOf( "." ) );
            nWidth = Integer.parseInt( sw );
            nHeight = Integer.parseInt( sh );
        } else {
            if(nWidth == 0) nWidth = aImgWidth;
            if(nHeight == 0) nHeight = aImgHeight;
        }
    }

    /**
     * <pre>
     * 파일로 저장 (파일 유형 변환)
     * </pre>
     * @param src
     * @return ImageIO
     * @throws IOException
     */
    public boolean saveImage( BufferedImage src ) throws IOException {
        return ImageIO.write( src, nTo == IMAGE_JPEG ? "jpg" : "png", new File( toFile ) );
    }

    /**
     * <pre>
     * supports JPEG 형으로 이미지 파일 압축 저장
     * </pre>
     * @param bufferedImage
     * @throws Exception
     */
    public void saveCompressedImage( BufferedImage bufferedImage ) throws Exception {
        // Image writer
//        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();
//        ImageOutputStream ios = ImageIO.createImageOutputStream(new File(toFile));
//        imageWriter.setOutput(ios);
//
//        // Compression
//        ImageWriteParam jpegParams = new JPEGImageWriteParam( Locale.getDefault() );
//        jpegParams.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
//        jpegParams.setCompressionQuality( nCompression );

        // Metadata (dpi)
//        IIOMetadata data = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(bufferedImage), jpegParams);
//        Element tree = (Element)data.getAsTree("javax_imageio_jpeg_image_1.0");
//        Element jfif = (Element)tree.getElementsByTagName("app0JFIF").item(0);
//        jfif.setAttribute("Xdensity", Integer.toString(1));
//        jfif.setAttribute("Ydensity", Integer.toString(2));
//        jfif.setAttribute("resUnits", "1"); // density is dots per inch
//        imageWriter.write( null, new IIOImage( bufferedImage, null, null ), jpegParams );
//
//        ios.flush();
//        imageWriter.dispose();
//        ios.close();
    }

    /**
     * <pre>
     * Image bgrImage
     * </pre>
     * @param bgrImg
     * @param source
     * @param aType
     * @param atX
     * @param atY
     * @return
     */
    public Image bgrImage( Image bgrImg, Image source, int aType, int atX, int atY ) {
        BufferedImage bi = null;
        Graphics2D g = null;
        bi = new BufferedImage( nWidth, nHeight, aType );
        g = bi.createGraphics();

        BufferedImage bgrImg2 = createBufferedImage( bgrImg, nFrom, nWidth, nHeight, 0, 0 );
        g.drawImage( bgrImg2, 0, 0, null );
        g.drawImage( source, atX, atY, null );
        return Toolkit.getDefaultToolkit().createImage( bi.getSource() );
    }

    /**
     * <pre>
     * Determines if the image has transparent pixels.
     * </pre>
     * @param image The image to check for transparent pixel.s
     * @return <code>true of false, according to the result
     * @throws InterruptedException
     */
    public boolean hasAlpha( Image src ) {
        try {
            PixelGrabber pg = new PixelGrabber( src, 0, 0, 1, 1, false );
            pg.grabPixels();
            return pg.getColorModel().hasAlpha();
        } catch ( InterruptedException e ) {
            return false;
        }
    }

//    //    // PSD 파일 변경
//    //    public void convertImgFromPSDFile() throws IOException {
//    //        PSDParser parser = new PSDParser( new FileInputStream( fromFile ) );
//    //        PSDLayerAndMask layerAndMask = parser.getLayerAndMask();
//    //
//    //        List<PSDLayerStructure> layers = layerAndMask.getLayers();
//    //        List<PSDLayerPixelData> images = layerAndMask.getImageLayers();
//    //        int i = 0;
//    //        new File( toFile ).mkdirs();
//    //        for ( PSDLayerStructure layer : layers ) {
//    //            PSDLayerPixelData pixelData = images.get( i );
//    //            BufferedImage image = pixelData.getImage();
//    //            if ( image != null ) {
//    //                ImageIO.write( image, toExt, new File( toFile + "/" + layer.getName() + "." + toExt ) );
//    //            }
//    //            i++;
//    //        }
//    //    }
//
    /**
     * <pre>
     * BufferedImage fillTransparentPixels
     * </pre>
     * @param image
     * @param fillColor
     * @return image2
     */
    public static BufferedImage fillTransparentPixels( BufferedImage image, Color fillColor ) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage image2 = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
        Graphics2D g = image2.createGraphics();
        g.setColor( fillColor );
        g.fillRect( 0, 0, w, h );
        g.drawRenderedImage( image, null );
        g.dispose();
        return image2;
    }

    /**
     * <pre>
     * 이미지 사이즈로 유효한 이미지 여부 판단 잘못 생성된 이미지일 경우 파일 size가 측정되지 않는 경우가 있음.
     * </pre>
     * @param fromFile 파일 경로+파일명
     * @return boolean true : 정상적인 image파일 false : size측정이 안되는 비정상적인 파일
     */
    public static boolean isValidImage( String fromFile ) {
        boolean returnValue = true;
        File fFile = new File( fromFile );
        int imgWidth = 0;
        int imgHeight = 0;
        try {
            Image img = new ImageIcon( fFile.getCanonicalPath() ).getImage();
            imgWidth = img.getWidth( null );
            imgHeight = img.getWidth( null );
        } catch ( IOException e ) {
            logger.error(e.getMessage());
        }
        if ( imgWidth <= 0 || imgHeight <= 0 ) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * main
     * @param args
     */
    public static void main( String[] args ) {
        String dir = "D:/KYOBO_mobile/workspace/com.tz.tz.framework/src/main/webapp/images/quality/";

        // 원래 크기 유지
        String fromFile = dir + "l9788996571308.jpg";
        String toFile = dir + "l9788996571308_02.jpg";

        // medium 크기 변경
        Map inputData = new HashMap();
        inputData.put( ImageUtil.FROM_FILE, fromFile );
        inputData.put( ImageUtil.TO_FILE, toFile );
//        inputData.put( ImageUtil.RATIO, 0.5F );
        ImageUtil imageUtil = new ImageUtil();
        imageUtil.convertImage( inputData );

//        // 크기 변경
//        inputData.put(ImageUtil.FROM_FILE, fromFile);
//        inputData.put(ImageUtil.TO_FILE, toFile);
//        inputData.put(ImageUtil.WIDTH, 215);
//        inputData.put(ImageUtil.HEIGHT, 10);
//        imageUtil.convertImage(inputData);
//
//        // thumb 크기 변경
//        inputData.put(ImageUtil.FROM_FILE, fromFile);
//        inputData.put(ImageUtil.TO_FILE, toFile);
//        inputData.put(ImageUtil.WIDTH, 160);
//        inputData.put(ImageUtil.HEIGHT, 163);
//        imageUtil.convertImage(inputData);
//
//        // 크기 변경 및 이동
//        inputData.put(ImageUtil.FROM_FILE, fromFile);
//        inputData.put(ImageUtil.TO_FILE, toFile);
//        inputData.put(ImageUtil.WIDTH, 260);
//        inputData.put(ImageUtil.HEIGHT, 263);
//        inputData.put(ImageUtil.ADDW, 20);
//        inputData.put(ImageUtil.ADDH, 25);
//        inputData.put(ImageUtil.RATIO, 0.5F);
//        inputData.put(ImageUtil.CROP_WIDTH, 30);
//        inputData.put(ImageUtil.CROP_HEIGHT, 30);
//        inputData.put(ImageUtil.CROPX, 10);
//        inputData.put(ImageUtil.CROPY, 10);
//        imageUtil.convertImage(inputData);
//
//        // 백그라운드 이미지 추가
//        inputData.put(ImageUtil.FROM_FILE, fromFile);
//        inputData.put(ImageUtil.TO_FILE, toFile);
//        inputData.put(ImageUtil.HEIGHT, 1504);
//        inputData.put(ImageUtil.WIDTH, 2000);
//        inputData.put(ImageUtil.RATIO, 0.5F);
//        inputData.put(ImageUtil.BGRX, 600);
//        inputData.put(ImageUtil.BGRY, 0);
//        inputData.put(ImageUtil.BGRFILE, dir + "blank.png");
//        imageUtil.convertImage( inputData );


    }
}

// http://www.codase.com/search/call?owner=javax.imageio.ImageWriter&start=10
// http://forums.sun.com/thread.jspa?threadID=5398197&tstart=0
// http://www.lumentier.com/java/ext/jdk-7-bld1/org.apache.xmlgraphics.image.writer.imageio/ImageIOImageWriter.IIOMultiImageWriter/class-javadoc.lmtr
// http://blog.alternativaplatform.com/en/2013/07/09/parser-psd-formata/
// http://www.velocityreviews.com/forums/t670059-help-for-java-imageio-png-image.html

//// 파일 SIZE 변경
//public void resizeImageFile( String fromFile, String toFile, int W ) throws IOException {
//  File fFile = new File( fromFile );
//  Image img = new ImageIcon( fFile.getCanonicalPath() ).getImage();
//  int w = img.getWidth( null );
//  int h = img.getHeight( null );
//  double ratio = (double)W / (double)w;
//
//  AffineTransform scale = AffineTransform.getScaleGroup( ratio, ratio );
//  BufferedImage scaled = new BufferedImage( W, (int)(h * ratio), BufferedImage.TYPE_INT_RGB );
//
//  Graphics2D g = scaled.createGraphics();
//  g.setTransform( scale );
//  g.drawImage( img, 0, 0, null );
//
//  File tFile = new File( fFile.getParentFile(), toFile );
//  JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( new FileOutputStream( tFile ) );
//  JPEGEncodeParam jpegParams = encoder.getDefaultJPEGEncodeParam( scaled );
//  jpegParams.setQuality( 1.0f, false );
//  encoder.setJPEGEncodeParam( jpegParams );
//  encoder.encode( scaled );
//}

//// Transparency 처리된 포맷 변경
//public void convertImageFile( String fromFile, String toFile, String aType ) throws IOException {
//    File file = new File( fromFile );
//    BufferedImage image = ImageIO.read( file );
//    int width = image.getWidth();
//    int height = image.getHeight();
//    BufferedImage jpgImage = null;
//
//    if ( GraphicsEnvironment.isHeadless() ) {
//        if ( image.getType() == BufferedImage.TYPE_CUSTOM ) {
//            jpgImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
//        } else {
//            jpgImage = new BufferedImage( width, height, image.getType() );
//        }
//    } else {
//        jpgImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( width, height, image.getTransparency() );
//    }
//    Graphics2D g2 = null;
//    try {
//        g2 = jpgImage.createGraphics();
//        g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC );
//        g2.drawImage( image, 0, 0, width, height, null );
//    } finally {
//        if ( g2 != null ) {
//            g2.dispose();
//        }
//    }
//    File f = new File( toFile );
//    ImageIO.write( jpgImage, aType, f );
//}

//private ImageManager imageManager;
//
//    private String basedir = "c:/";
//
//    private String source;
//
//    public final int LARGE_WIDTH = 350;
//
//    public final int MIDIUM_WIDTH = 180;
//
//    public final int SAMLL_WIDTH = 75;
//
//    public final int INDEX_HEIGHT = 75;
//
//    public final int FLAGSHIP_HEIGHT = 75;
//
//    public ImageTest() {
//        //The ImageManager is set up for the whole application
//        this.imageManager = new ImageManager( new DefaultImageContext() );
//    }

//          String fromFile = "C:/TZ_WEB/workspace/LGEH_FRONT/webapp/common/images/bug.jpg";
//          String toFile = "C:/TZ_WEB/workspace/LGEH_FRONT/webapp/common/images/bug.gif";

//Image image = Toolkit.getDefaultToolkit().getImage( fromFile );
//BufferedImage source = new BufferedImage( 60, 60, BufferedImage.TYPE_BYTE_INDEXED );
//Graphics g = source.getGraphics();
//g.drawImage( image, 0, 0, null );
//
//BufferedImage target = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
//Graphics2D g2 = target.createGraphics();
//g2.drawRenderedImage(source, null);
//g2.dispose();

//            File f = new File( fromFile );
//            ImageReader ir = ImageIO.getImageReadersByFormatName( "gif" ).next();
//            ByteArrayInputStream bais = new ByteArrayInputStream( FileUtil.getBytesFromFile( f ) );
//            ImageInputStream iis = ImageIO.createImageInputStream( bais );
//            ir.setInput( iis );
//
//            Image image = Toolkit.getDefaultToolkit().getImage( fromFile );
//
//            //            BufferedImage image2 = ImageUtils.createBufferedImage(image, 0, 100, 100);
//
////            BufferedImage image2 = new BufferedImage( image.getWidth( null ), image.getHeight( null ), BufferedImage.TYPE_INT_ARGB );
//            BufferedImage image2 = new BufferedImage( 60, 60, BufferedImage.TYPE_INT_RGB );
//            Graphics g = image2.getGraphics();
//            g.drawImage( image, 0, 0, null );
//
//            //            ImageUtils.saveCompressedImage(image2, toFile, 0);
//
//            Iterator iter = ImageIO.getImageWritersByFormatName( "jpg" );
//            ImageWriter writer = (ImageWriter)iter.next();
//            ImageOutputStream ios = ImageIO.createImageOutputStream( new File( toFile ) );
//            writer.setOutput( ios );
//            ImageWriteParam iwparam = new JPEGImageWriteParam( Locale.getDefault() );
//            iwparam.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
//            iwparam.setCompressionQuality( 0.7F );
//            writer.write( null, new IIOImage( image2, null, null ), iwparam );
//            ios.flush();
//            writer.dispose();
//            ios.close();

//      String uri = f.toURI().toASCIIString();
//      ImageTest test = new ImageTest();
//
//      ImageSessionContext imageContext = new DefaultImageSessionContext(
//      test.imageManager.getImageContext(), null);
//
//      ImageInfo info = test.imageManager.preloadImage(uri, imageContext);
//
//      ImageLoader loader = new ImageLoaderImageIO(ImageFlavor.RENDERED_IMAGE);
//      ImageProviderPipeline pipeline = new ImageProviderPipeline(test.imageManager.getCache(), loader);
//      pipeline.addConverter(new ImageConverterRendered2PNG());
//      Image img = pipeline.execute(info, null, imageContext);

//            BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_GRAY);
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            ImageOutputStream imageOutput = new MemoryCacheImageOutputStream(output);
//            String contentType = "image/jpeg";
//            Iterator writers = ImageIO.getImageWritersByMIMEType(contentType);   //contentType
//            ImageWriter writer = null;
//            if (writers.hasNext()) {
//            writer = (ImageWriter)writers.next();
//            } else {
//                logger.debug("Content type not supported: " + contentType);
//            }
//            writer.setOutput(imageOutput);
//            writer.write(image);
//            String result = output.toString("ISO8859_1");
//            writer.dispose();
//            output.close();

//            OutputStream out = new NullOutputStream();
//            org.apache.xmlgraphics.image.writer.ImageWriter imageWriter = new TIFFImageWriter();
//            MultiImageWriter writer = null;
//            try {
//                writer = imageWriter.createMultiImageWriter(out);
//                // retrieve writer
//                if (imageWriter != null) {
//                    ImageWriterParams iwp = new ImageWriterParams();
//                    iwp.setCompressionMethod("JPEG");
//
//                    for (int pageNumber = 0; pageNumber <= 2; pageNumber++) {
//                        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_GRAY);
//                        writer.writeImage(image, iwp);
//                    }
//                }
//            } finally {
//                writer.close();
//            }

//            String uri = f.toURI().toASCIIString();
//
//            ImageTest test = new ImageTest();
//
//            ImageSessionContext sessionContext = new DefaultImageSessionContext(
//                    test.imageManager.getImageContext(), null);
//
//            ImageInfo info = test.imageManager.getImageInfo(uri, sessionContext);
//
//            Image img = test.imageManager.getImage(
//                    info, ImageFlavor.GRAPHICS2D, sessionContext);
//            ImageFlavor[] flavors = new ImageFlavor[1];
//            flavors[0] = ImageFlavor.RAW_PNG;
//            Image img2 = test.imageManager.convertImage( img, flavors );
//            ImageInfo info2 = img2.getInfo();
//            logger.debug("0");

//    public void createProduct(HashMap modelImage) throws Exception {
//        String timeMile = String.valueOf(System.currentTimeMillis());
//
//        /*
//         * base product image
//         */
////        modelImage.setImageSmallNM(modelImage.getSalesCode().toLowerCase() + "_" + timeMile + "_s.jpg");
////        modelImage.setImageMediumNM(modelImage.getSalesCode().toLowerCase() + "_" + timeMile + "_m.jpg");
////        modelImage.setImageLargeNM(modelImage.getSalesCode().toLowerCase() + "_" + timeMile + "_l.jpg");
//
//        createWidth(basedir + "/" + modelImage.get("ImageSmallNM"), ImageTest.SAMLL_WIDTH);
//        createWidth(basedir + "/" + modelImage.get("ImageMediumNM"), ImageTest.MIDIUM_WIDTH);
//        createWidth(basedir + "/" + modelImage.get("ImageLargeNM"), ImageTest.LARGE_WIDTH);
//
//        /*
//         * index page & flagship image
//         */
////        modelImage.setImageHot01NM(modelImage.getSalesCode().toLowerCase() + "_" + timeMile + "_i.jpg");
////        modelImage.setImageHot02NM(modelImage.getSalesCode().toLowerCase() + "_" + timeMile + "_f.jpg");
//
////        createHeight(basedir + "/" + modelImage.getImageHot01NM(), ImageTest.INDEX_HEIGHT);
////        createHeight(basedir + "/" + modelImage.getImageHot02NM(), ImageTest.FLAGSHIP_HEIGHT);
//    }
//
//    /**
//     * 이미지 크기를 생성한다.
//     *
//     * @param target
//     * @param targetW
//     * @throws Exception
//     */
//    public void createWidth(String target, int targetW) throws Exception {
//        logger.debug("createWidth() -> ImageIcon start");
//        logger.debug("this.basedir : "+this.basedir);
//        logger.debug("this.source : "+this.source);
//        Image imgSource = new ImageIcon(this.basedir + "/" + this.source).getImage();
//        imgSource.flush();
//        logger.debug("createWidth() -> ImageIcon end");
//        int oldW = imgSource.getWidth(null);
//        int oldH = imgSource.getHeight(null);
//
//        int newW = targetW;
//        int newH = (targetW * oldH) / oldW;
//
//        createImage(imgSource, target, newW, newH);
//    }
//
//    public void createHeight(String target, int targetH) throws Exception {
//        Image imgSource = new ImageIcon(this.basedir + "/" + this.source).getImage();
//        imgSource.flush();
//
//        int oldW = imgSource.getWidth(null);
//        int oldH = imgSource.getHeight(null);
//
//        int newW = (targetH * oldW) / oldH;
//        int newH = targetH;
//
//        createImage(imgSource, target, newW, newH);
//    }
//
//    public void create(String target, int targetW, int targetH) throws Exception {
//        Image imgSource = new ImageIcon(this.basedir + "/" + this.source).getImage();
//
//        int oldW = imgSource.getWidth(null);
//        int oldH = imgSource.getHeight(null);
//
//        int newW = targetW;
//        int newH = (targetW * oldH) / oldW;
//        if (targetH > targetW) {
//            newW = (targetH * oldW) / oldH;
//            newH = targetH;
//        }
//
//        createImage(imgSource, target, newW, newH);
//    }
//
//    /**
//     * 주어진 이미지를 생성한다.
//     *
//     * @param imgSource
//     * @param target
//     * @param newW
//     * @param newH
//     * @throws Exception
//     */
//    private void createImage(Image imgSource, String target, int newW, int newH) throws Exception {
//        try {
//            Image imgTarget = imgSource.getScaledGroup(newW, newH, Image.SCALE_SMOOTH);
//
//            int pixels[] = new int[newW * newH];
//
//            PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, newW, newH, pixels, 0, newW);
//            pg.grabPixels();
//
//            BufferedImage bi = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
//            bi.setRGB(0, 0, newW, newH, pixels, 0, newW);
//
//            FileOutputStream fos = new FileOutputStream(target);
//            JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(fos);
//            JPEGEncodeParam jep = jpeg.getDefaultJPEGEncodeParam(bi);
//            jep.setQuality(1, false);
//            jpeg.encode(bi, jep);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("Can't create image!!!");
//        }
//    }
//
//public void createImage( Image imgSource, String target, int newW, int newH ) throws Exception {
//  try {
//      Image imgTarget = imgSource.getScaledGroup( newW, newH, Image.SCALE_SMOOTH );
//      int pixels[] = new int[newW * newH];
//      PixelGrabber pg = new PixelGrabber( imgTarget, 0, 0, newW, newH, pixels, 0, newW );
//      pg.grabPixels();
//      BufferedImage bi = new BufferedImage( newW, newH, BufferedImage.TYPE_INT_RGB );
//      bi.setRGB( 0, 0, newW, newH, pixels, 0, newW );
//      FileOutputStream fos = new FileOutputStream( target );
//      JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder( fos );
//      JPEGEncodeParam jep = jpeg.getDefaultJPEGEncodeParam( bi );
//      jep.setQuality( 1, false );
//      jpeg.encode( bi, jep );
//      fos.close();
//  } catch ( Exception e ) {
//      e.printStackTrace();
//      throw new Exception( "Can't create image!!!" );
//  }
//}