package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2ab.DoubleMapPQ;
import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2c.utils.Constants.*;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};

    private static final String IMAGE_NAME_FORMAT = "d%d_x%d_y%d.png";

    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    // 1. read parameters from request
    // 2. calculate LonDPP from parameters
    // 3. make sure depth according to step#2
    // 4. calculate "raster_ul_lon", "raster_ul_lat" ...
    // 5. decide if query success
    // 6. according to step#4, find the image files
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        System.out.println("yo, wanna know the parameters given by the web browser? They are:");
        System.out.println(requestParams);
        Map<String, Object> results = new HashMap<>();
        // store parameter
        Double ullat = requestParams.get(REQUIRED_RASTER_REQUEST_PARAMS[0]);
        Double ullon = requestParams.get(REQUIRED_RASTER_REQUEST_PARAMS[1]);
        Double lrlat = requestParams.get(REQUIRED_RASTER_REQUEST_PARAMS[2]);
        Double lrlon = requestParams.get(REQUIRED_RASTER_REQUEST_PARAMS[3]);
        // edge case: no interactive with ROOT scope
        if(ullon >= ROOT_LRLON || lrlon <= ROOT_ULLON || ullat <= ROOT_LRLAT || lrlat >= ROOT_ULLAT) {
            return queryFail();
        }
        Double width = requestParams.get(REQUIRED_RASTER_REQUEST_PARAMS[4]);
        Double height = requestParams.get(REQUIRED_RASTER_REQUEST_PARAMS[5]);
        // calculate LonDPP
        double targetLonDPP = (lrlon - ullon)/width;
        // find depth
        int depth = 0;
        for(depth = 0; depth <= 7; depth++) {
            double depthLonDPP = (ROOT_LRLON - ROOT_ULLON)/(Math.pow(2, depth) * TILE_SIZE);
            if(depthLonDPP <= targetLonDPP) {
                break;
            }
        }
        if(depth > 7) {
            depth = 7;
        }

        if (depth == 0) {
            results.put("render_grid", new String[][]{{"d0_x0_y0.png"}});
            results.put("raster_ul_lon", ROOT_ULLON);
            results.put("raster_ul_lat", ROOT_ULLAT);
            results.put("raster_lr_lon", ROOT_LRLON);
            results.put("raster_lr_lat", ROOT_LRLAT);
        } else {
            // according to depth, ullat, ullon, lrlat, lrlon, calculate the images
            double ROOT_HEIGHT_UNIT = (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2, depth);
            double ROOT_WIDTH_UNIT = (ROOT_LRLON - ROOT_ULLON) / Math.pow(2, depth);
            int ullatIndex = (int)((ROOT_ULLAT - ullat) / ROOT_HEIGHT_UNIT);
            int lrlatIndex = Math.min((int)((ROOT_ULLAT - lrlat)/ ROOT_HEIGHT_UNIT), (int) Math.pow(2, depth)-1);
            int ullonIndex = (int)((ullon - ROOT_ULLON) / ROOT_WIDTH_UNIT);
            int lrlonIndex = Math.min((int)((lrlon - ROOT_ULLON) / ROOT_WIDTH_UNIT), (int) Math.pow(2, depth)-1);
            String[][] render_grid = new String[lrlatIndex - ullatIndex + 1][lrlonIndex - ullonIndex + 1];
            for(int i = ullatIndex; i <= lrlatIndex; i++) {
                for(int j = ullonIndex; j <= lrlonIndex; j++) {
                    render_grid[i - ullatIndex][j - ullonIndex] = String.format(IMAGE_NAME_FORMAT, depth, j, i);
                }
            }
            results.put("render_grid", render_grid);
            results.put("raster_ul_lon", ullonIndex * ROOT_WIDTH_UNIT + ROOT_ULLON);
            results.put("raster_ul_lat", ROOT_ULLAT - ullatIndex * ROOT_HEIGHT_UNIT );
            results.put("raster_lr_lon", (lrlonIndex+1) * ROOT_WIDTH_UNIT + ROOT_ULLON);
            results.put("raster_lr_lat", ROOT_ULLAT - (lrlatIndex+1) * ROOT_HEIGHT_UNIT);
        }
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
