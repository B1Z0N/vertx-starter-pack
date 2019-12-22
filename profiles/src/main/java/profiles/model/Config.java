package profiles.model;

import com.amazonaws.regions.Regions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/** Data class: representation of conf/config.json in plain java class */
public class Config {

  // Constants

  private static final String ENDPOINT = "endpoint";
  private static final String ENDPOINT_HOST = "host";
  private static final String ENDPOINT_PORT = "port";
  private static final String TLS = "tls";
  private static final String TLS_CERT_CHAIN = "cert_chain";
  private static final String TLS_PRIV_KEY = "priv_key";
  private static final String TLS_CA = "ca";

  private static final String SIZES = "sizes";
  private static final String NAME = "name";
  private static final String WIDTH = "width";

  private static final String AWS = "aws";
  private static final String PHOTOS_BUCKET = "photosBucket";
  private static final String USERPIC_BUCKET = "userpicsBucket";
  private static final String REGION = "region";
  private static final String EXTENSION = "extension";

  // Variables

  private final JsonObject mConfigObject;
  private final String mEndpointHost;
  private final String mEndpointPort;
  private final String mTlsCertChain;
  private final String mTlsPrivKey;
  private final String mTlsCa;

  private final HashMap<String, Integer> mSizes;
  private final Regions mRegion;
  private final String mPhotosBucket;
  private final String mUserpicBucket;
  private final String mExtension;

  // Constructors

  public Config(@Nonnull JsonObject config) {
    mConfigObject = config;

    JsonObject endpoint = config.getJsonObject(ENDPOINT);
    mEndpointHost = endpoint.getString(ENDPOINT_HOST);
    mEndpointPort = endpoint.getString(ENDPOINT_PORT);

    JsonObject tls = config.getJsonObject(TLS);
    mTlsCertChain = tls.getString(TLS_CERT_CHAIN);
    mTlsPrivKey = tls.getString(TLS_PRIV_KEY);
    mTlsCa = tls.getString(TLS_CA);

    JsonArray sizes = config.getJsonArray(SIZES);
    mSizes = jsonSizesArrayToMap(sizes);

    JsonObject aws = config.getJsonObject(AWS);
    mRegion = Regions.valueOf(aws.getString(REGION));
    mUserpicBucket = aws.getString(USERPIC_BUCKET);
    mPhotosBucket = aws.getString(PHOTOS_BUCKET);

    mExtension = config.getString(EXTENSION);
  }

  private HashMap<String, Integer> jsonSizesArrayToMap(@Nonnull JsonArray jarr) {
    HashMap<String, Integer> map = new HashMap<>();
    for (int i = 0; i < jarr.size(); i++) {
      JsonObject current = jarr.getJsonObject(i);
      map.put(current.getString(NAME), current.getInteger(WIDTH));
    }

    return map;
  }

  private JsonArray mapSizesToJsonArray(@Nonnull HashMap<String, Integer> map) {
    JsonArray jarr = new JsonArray();
    Iterator it = map.entrySet().iterator();
    while (it.hasNext()) {
      HashMap.Entry pair = (HashMap.Entry) it.next();
      jarr.add(
            new JsonObject()
              .put(NAME, pair.getKey())
              .put(WIDTH, pair.getValue())
      );
      it.remove(); // avoids a ConcurrentModificationException
    }

    return jarr;
  }

  // Public

  public JsonObject toJson() {
    JsonObject endpoint = new JsonObject()
      .put(ENDPOINT_HOST, mEndpointHost)
      .put(ENDPOINT_PORT, mEndpointPort);

    JsonObject tls = new JsonObject()
      .put(TLS_CERT_CHAIN, mTlsCertChain)
      .put(TLS_PRIV_KEY, mTlsPrivKey)
      .put(TLS_CA, mTlsCa);

    JsonArray sizes = mapSizesToJsonArray(mSizes);

    return new JsonObject()
      .put(ENDPOINT, endpoint)
      .put(TLS, tls)
      .put(SIZES, sizes);
  }

  // Accessors

  JsonObject getConfigObject() {
    return mConfigObject;
  }

  public String getEndpointHost() {
    return mEndpointHost;
  }

  public String getEndpointPort() {
    return mEndpointPort;
  }

  public String getTlsCertChain() {
    return mTlsCertChain;
  }

  public String getTlsPrivKey() {
    return mTlsPrivKey;
  }

  public String getTlsCa() {
    return mTlsCa;
  }

  public HashMap<String, Integer> getSizes() {
    return mSizes;
  }

  public Regions getRegion() {
    return mRegion;
  }

  public String getPhotosBucket() {
    return mPhotosBucket;
  }

  public String getUserpicBucket() {
    return mUserpicBucket;
  }

  public String getExtension() {
    return mExtension;
  }
  // Utils

  @Override
  public String toString() {
    return "Config{" +
      "mEndpointHost='" + mEndpointHost + '\'' +
      ", mEndpointPort=" + mEndpointPort +
      ", mTlsCertChain='" + mTlsCertChain + '\'' +
      ", mTlsPrivKey='" + mTlsPrivKey + '\'' +
      ", mTlsCa='" + mTlsCa + '\'' +
      ", mSizes='" + mSizes.toString() + '\'' +
      '}';
  }
}
