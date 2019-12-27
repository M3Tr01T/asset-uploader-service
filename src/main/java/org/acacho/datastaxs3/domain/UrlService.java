package org.acacho.datastaxs3.domain;

import java.net.URL;

public interface UrlService {

  URL retrieveUrlForUpload(String key);

  URL retrieveUrlForDownload(String key, int signatureDuration);
}
