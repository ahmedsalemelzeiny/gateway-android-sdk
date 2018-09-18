package com.mastercard.gateway.android.sdk;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class Gateway3DSecurePresenter {

    static final String REDIRECT_SCHEME = "gatewaysdk";

    Gateway3DSecureView view;

    void attachView(Gateway3DSecureView view) {
        this.view = view;

        // init html
        String extraHtml = view.getExtraHtml();
        if (extraHtml == null) {
            view.cancel();
            return;
        } else {
            view.setWebViewHtml(extraHtml);
        }

        // init title
        String defaultTitle = view.getDefaultTitle();
        String extraTitle = view.getExtraTitle();
        view.setToolbarTitle(extraTitle != null ? extraTitle : defaultTitle);
    }

    void detachView() {
        view = null;
    }

    void webViewUrlChanges(Uri uri) {
        String scheme = uri.getScheme();
        if (REDIRECT_SCHEME.equalsIgnoreCase(scheme)) {
            view.complete(parseQueryString(uri));
        } else if ("mailto".equalsIgnoreCase(scheme)) {
            view.intentToEmail(uri);
        } else {
            view.loadWebViewUrl(uri);
        }
    }

    Map<String, String> parseQueryString(Uri uri) {
        Map<String, String> data = new HashMap<>();

        Set<String> params = uri.getQueryParameterNames();
        for (String param : params) {
            data.put(param, uri.getQueryParameter(param));
        }

        return data;
    }
}
