/*
*
* Paros and its related class files.
* 
* Paros is an HTTP/HTTPS proxy for assessing web application security.
* Copyright (C) 2003-2004 Chinotec Technologies Company
* 
* This program is free software; you can redistribute it and/or
* modify it under the terms of the Clarified Artistic License
* as published by the Free Software Foundation.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* Clarified Artistic License for more details.
* 
* You should have received a copy of the Clarified Artistic License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
// ZAP: 2011/06/02 Warn the first time the user double clicks on a tab
// ZAP: 2012/03/15 Removed the options of the http panels.
// ZAP: 2012/08/01 Issue 332: added support for Modes
// ZAP: 2013/01/25 Removed the "(non-Javadoc)" comments.
// ZAP: 2013/07/23 Issue 738: Options to hide tabs
// ZAP: 2013/12/13 Added support for optional names in tabs.
// ZAP: 2014/03/23 Issue 589: Move Reveal extension to ZAP extensions project
// ZAP: 2014/04/25 Issue 642: Add timestamps to Output tab(s)
// ZAP: 2014/10/07 Issue 1357: Hide unused tabs
// ZAP: 2014/10/09 Issue 1359: Options for splash screen
// ZAP: 2014/12/16 Issue 1466: Config option for 'large display' size
// ZAP: 2015/03/04 Added dev build warning option
// ZAP: 2016/04/04 Do not require a restart to show/hide the tool bar
// ZAP: 2016/04/06 Fix layouts' issues
// ZAP: 2016/04/27 Save, always, the Locale as String
// ZAP: 2016/05/13 Add options to confirm removal of exclude from proxy, scanner and spider regexes
// ZAP: 2017/05/29 Add option to use system's locale for formatting.
// ZAP: 2017/09/26 Use helper methods to read the configurations.
// ZAP: 2018/01/25 Remove unused constant LOCALES.
// ZAP: 2018/02/14 Remove unnecessary boxing / unboxing
// ZAP: 2018/02/27 Added support for selecting the look and feel.

package org.parosproxy.paros.extension.option;

import java.util.Locale;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.common.AbstractParam;
import org.parosproxy.paros.control.Control.Mode;
import org.parosproxy.paros.view.WorkbenchPanel;
import org.zaproxy.zap.extension.httppanel.view.largerequest.LargeRequestUtil;
import org.zaproxy.zap.extension.httppanel.view.largeresponse.LargeResponseUtil;

// ZAP: Added support for selecting the locale

public class OptionsParamView extends AbstractParam {
	
	private static final String DEFAULT_TIME_STAMP_FORMAT =  Constant.messages.getString("timestamp.format.default");
	
	public static final String BASE_VIEW_KEY = "view";

	private static final String SHOW_TEXT_ICONS = "view.showTabNames";
	private static final String PROCESS_IMAGES = "view.processImages";
	public static final String LOCALE = "view.locale";
	public static final String DISPLAY_OPTION = "view.displayOption";
	private static final String RESPONSE_PANEL_POS_KEY = BASE_VIEW_KEY + ".messagePanelsPosition.lastSelectedPosition";
	public static final String BRK_PANEL_VIEW_OPTION = "view.brkPanelView";
	public static final String SHOW_MAIN_TOOLBAR_OPTION = "view.showMainToolbar";
	public static final String DEFAULT_LOCALE = "en_GB";
	public static final String ADVANCEDUI_OPTION = "view.advancedview";
	public static final String WMUIHANDLING_OPTION = "view.uiWmHandling";
	public static final String ASKONEXIT_OPTION = "view.askOnExit";
	public static final String WARN_ON_TAB_DOUBLE_CLICK_OPTION = "view.warnOnTabDoubleClick";
	public static final String MODE_OPTION = "view.mode";
	public static final String TAB_PIN_OPTION = "view.tab.pin";
	public static final String OUTPUT_TAB_TIMESTAMPING_OPTION = "view.outputTabsTimeStampsOption"; 
	public static final String OUTPUT_TAB_TIMESTAMP_FORMAT = "view.outputTabsTimeStampsFormat"; 

	/**
	 * The configuration key used to save/load the option {@link #showLocalConnectRequests}.
	 */
	private static final String SHOW_LOCAL_CONNECT_REQUESTS = "view.showLocalConnectRequests";

	/**
	 * The configuration key used to save/load the option {@link #useSystemsLocaleForFormat}.
	 */
    private static final String USE_SYSTEMS_LOCALE_FOR_FORMAT_KEY = BASE_VIEW_KEY + ".usesystemslocaleformat";

	public static final String SPLASHSCREEN_OPTION = "view.splashScreen";
	public static final String LARGE_REQUEST_SIZE = "view.largeRequest";
	public static final String LARGE_RESPONSE_SIZE = "view.largeResponse";
	public static final String FONT_NAME = "view.fontName";
	public static final String FONT_SIZE = "view.fontSize";
	public static final String SCALE_IMAGES = "view.scaleImages";
	public static final String SHOW_DEV_WARNING = "view.showDevWarning";
	public static final String LOOK_AND_FEEL = "view.lookAndFeel";
	private static final String CONFIRM_REMOVE_PROXY_EXCLUDE_REGEX_KEY = "view.confirmRemoveProxyExcludeRegex";
    private static final String CONFIRM_REMOVE_SCANNER_EXCLUDE_REGEX_KEY = "view.confirmRemoveScannerExcludeRegex";
    private static final String CONFIRM_REMOVE_SPIDER_EXCLUDE_REGEX_KEY = "view.confirmRemoveSpiderExcludeRegex";

	private int advancedViewEnabled = 0;
	private int processImages = 0;
	private int showMainToolbar = 1;
	private String configLocale = "";
	private String locale = "";
	private int displayOption = 0;
	private String responsePanelPosition;
	private int brkPanelViewOption = 0;
	private int askOnExitEnabled = 1;
	private int wmUiHandlingEnabled = 0;
	private boolean warnOnTabDoubleClick = false;
    private boolean showTabNames = true;
	private String mode = Mode.standard.name();
	private boolean outputTabTimeStampingEnabled = false; 
	private String outputTabTimeStampFormat = DEFAULT_TIME_STAMP_FORMAT; 

	/**
	 * Flag that indicates if the HTTP CONNECT requests received by the local proxy should be (persisted and) shown in the UI.
	 * 
	 * @see #SHOW_LOCAL_CONNECT_REQUESTS
	 * @see #isShowLocalConnectRequests()
	 * @see #setShowLocalConnectRequests(boolean)
	 */
	private boolean showLocalConnectRequests;
	
    private boolean showSplashScreen = true;
    private int largeRequestSize = LargeRequestUtil.DEFAULT_MIN_CONTENT_LENGTH;
    private int largeResponseSize = LargeResponseUtil.DEFAULT_MIN_CONTENT_LENGTH;
    private int fontSize = -1;
    private String fontName = "";
    private boolean scaleImages = true;
    private boolean showDevWarning = true;
    private String lookAndFeel = "";
	private boolean confirmRemoveProxyExcludeRegex;
    private boolean confirmRemoveScannerExcludeRegex;
    private boolean confirmRemoveSpiderExcludeRegex;

    /**
     * Flag that indicates if the system's locale should be used for formatting.
     * 
     * @see #USE_SYSTEMS_LOCALE_FOR_FORMAT_KEY
     * @see #isUseSystemsLocaleForFormat()
     * @see #setUseSystemsLocaleForFormat(boolean)
     */
    private boolean useSystemsLocaleForFormat;
	
    public OptionsParamView() {
    }

    @Override
	protected void parse() {
      	showTabNames = getBoolean(SHOW_TEXT_ICONS, true);
	    processImages = getInt(PROCESS_IMAGES, 0);
	    configLocale = getString(LOCALE, null);	// No default
	    locale = getString(LOCALE, DEFAULT_LOCALE);
	    useSystemsLocaleForFormat = getBoolean(USE_SYSTEMS_LOCALE_FOR_FORMAT_KEY, true);
	    displayOption = getInt(DISPLAY_OPTION, 0);
        responsePanelPosition = getString(RESPONSE_PANEL_POS_KEY, WorkbenchPanel.ResponsePanelPosition.TABS_SIDE_BY_SIDE.name());
	    brkPanelViewOption = getInt(BRK_PANEL_VIEW_OPTION, 0);
	    showMainToolbar = getInt(SHOW_MAIN_TOOLBAR_OPTION, 1);
	    advancedViewEnabled = getInt(ADVANCEDUI_OPTION, 0);
	    wmUiHandlingEnabled = getInt(WMUIHANDLING_OPTION, 0);
	    askOnExitEnabled = getInt(ASKONEXIT_OPTION, 1);
	    warnOnTabDoubleClick = getBoolean(WARN_ON_TAB_DOUBLE_CLICK_OPTION, true);
	    mode = getString(MODE_OPTION, Mode.standard.name());
	    outputTabTimeStampingEnabled = getBoolean(OUTPUT_TAB_TIMESTAMPING_OPTION, false); 
	    outputTabTimeStampFormat = getString(OUTPUT_TAB_TIMESTAMP_FORMAT, DEFAULT_TIME_STAMP_FORMAT);

        showLocalConnectRequests = getBoolean(SHOW_LOCAL_CONNECT_REQUESTS, false);

	    showSplashScreen = getBoolean(SPLASHSCREEN_OPTION, true);
	    largeRequestSize = getInt(LARGE_REQUEST_SIZE, LargeRequestUtil.DEFAULT_MIN_CONTENT_LENGTH);
	    largeResponseSize = getInt(LARGE_RESPONSE_SIZE, LargeResponseUtil.DEFAULT_MIN_CONTENT_LENGTH);
	    fontSize = getInt(FONT_SIZE, -1);
	    fontName = getString(FONT_NAME, "");
	    scaleImages = getBoolean(SCALE_IMAGES, true);
	    showDevWarning = getBoolean(SHOW_DEV_WARNING, true);
	    lookAndFeel = getString(LOOK_AND_FEEL,"");
	    // Special cases - set via static methods
	    LargeRequestUtil.setMinContentLength(largeRequestSize);
	    LargeResponseUtil.setMinContentLength(largeResponseSize);

        this.confirmRemoveProxyExcludeRegex = getBoolean(CONFIRM_REMOVE_PROXY_EXCLUDE_REGEX_KEY, false);

        this.confirmRemoveScannerExcludeRegex = getBoolean(CONFIRM_REMOVE_SCANNER_EXCLUDE_REGEX_KEY, false);

        this.confirmRemoveSpiderExcludeRegex = getBoolean(CONFIRM_REMOVE_SPIDER_EXCLUDE_REGEX_KEY, false);
    }

	/**
	 * @return Returns the skipImage.
	 */
	public int getProcessImages() {
		return processImages;
	}
	
	/**
	 * @param processImages 0 = not to process.  Other = process images
	 * 
	 */
	public void setProcessImages(int processImages) {
		this.processImages = processImages;
		getConfig().setProperty(PROCESS_IMAGES, Integer.toString(processImages));
	}
	
	public boolean isProcessImages() {
		return !(processImages == 0);
	}
	
	/**
	 * @deprecated (2.5.0) Use {@link #isShowMainToolbar()} instead. It will be removed in a future release.
	 */
	@Deprecated
	@SuppressWarnings("javadoc")
	public int getShowMainToolbar() {
		return showMainToolbar;
	}
	
	/**
	 * Tells whether or not the main tool bar should be shown.
	 *
	 * @return {@code true} if the main tool bar should be shown, {@code false} otherwise.
	 * @since 2.5.0
	 */
	public boolean isShowMainToolbar() {
		return showMainToolbar != 0;
	}

	/**
	 * @deprecated (2.5.0) Use {@link #setShowMainToolbar(boolean)} instead. It will be removed in a future release.
	 */
	@Deprecated
	@SuppressWarnings("javadoc")
	public void setShowMainToolbar(int showMainToolbar) {
		setShowMainToolbar(showMainToolbar != 0);
	}

	/**
	 * Sets whether or not the main tool bar should be shown.
	 *
	 * @param show {@code true} if the main tool bar should be shown, {@code false} otherwise.
	 * @since 2.5.0
	 */
	public void setShowMainToolbar(boolean show) {
		this.showMainToolbar = show ? 1 : 0;
		getConfig().setProperty(SHOW_MAIN_TOOLBAR_OPTION, showMainToolbar);
	}

	
	/**
	 * @return the locale, which should be used. 
	 *         It will return a default value, if nothing was configured yet. 
	 *         Never null
	 * @see #getConfigLocale()
	 */
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		if (locale != null) {
			this.locale = locale;
			getConfig().setProperty(LOCALE, locale);
		}
	}
	
	public void setLocale(Locale locale) {
		if (locale != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(locale.getLanguage());
			if (locale.getCountry().length() > 0) sb.append("_").append(locale.getCountry());
			if (locale.getVariant().length() > 0) sb.append("_").append(locale.getVariant());
			setLocale(sb.toString());
		}
	}

	/**
	 * @return The really configured locale, can be null
	 * @see #getLocale()
	 */
	public String getConfigLocale() {
		return configLocale;
	}

	public boolean getShowTabNames() {
		return showTabNames;
	}
	
	public void setShowTabNames(boolean showTabNames) {
		this.showTabNames = showTabNames;
		getConfig().setProperty(SHOW_TEXT_ICONS, showTabNames);
	}

	public int getBrkPanelViewOption() {
		return brkPanelViewOption;
	}
	
	public void setBrkPanelViewOption(int brkPanelViewIdx) {
		brkPanelViewOption = brkPanelViewIdx;
		getConfig().setProperty(BRK_PANEL_VIEW_OPTION, Integer.toString(brkPanelViewOption));
	}
	
	public int getDisplayOption() {
		return displayOption;
	}

	public void setDisplayOption(int displayOption) {
		this.displayOption = displayOption;
		getConfig().setProperty(DISPLAY_OPTION, Integer.toString(displayOption));
	}

	/**
	 * Gets the name of the current response panel position.
	 *
	 * @return the name of the current position
	 * @since 2.5.0
	 * @see org.parosproxy.paros.view.WorkbenchPanel.ResponsePanelPosition
	 */
	public String getResponsePanelPosition() {
		return responsePanelPosition;
	}

	/**
	 * Sets the name of the current response panel position.
	 * 
	 * @param position the name of the position
	 * @since 2.5.0
	 */
	public void setResponsePanelPosition(String position) {
		this.responsePanelPosition = position;
		getConfig().setProperty(RESPONSE_PANEL_POS_KEY, position);
	}
	
	public int getAdvancedViewOption() {
		return advancedViewEnabled;
	}
	
	public void setAdvancedViewOption(int isEnabled) {
		advancedViewEnabled = isEnabled;
		getConfig().setProperty(ADVANCEDUI_OPTION, Integer.toString(isEnabled));
	}

	public void setAskOnExitOption(int isEnabled) {
		askOnExitEnabled = isEnabled;
		getConfig().setProperty(ASKONEXIT_OPTION, Integer.toString(isEnabled));
	}

	public int getAskOnExitOption() {
		return askOnExitEnabled;
	}

	public void setWmUiHandlingOption(int isEnabled) {
		wmUiHandlingEnabled = isEnabled;
		getConfig().setProperty(WMUIHANDLING_OPTION, Integer.toString(isEnabled));
	}
	
	public int getWmUiHandlingOption() {
		return wmUiHandlingEnabled;
	}

	public boolean getWarnOnTabDoubleClick() {
		return warnOnTabDoubleClick;
	}

	public void setWarnOnTabDoubleClick(boolean warnOnTabDoubleClick) {
		this.warnOnTabDoubleClick = warnOnTabDoubleClick;
		getConfig().setProperty(WARN_ON_TAB_DOUBLE_CLICK_OPTION, warnOnTabDoubleClick);
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
		getConfig().setProperty(MODE_OPTION, mode);
	}
	
	public void setOutputTabTimeStampingEnabled(boolean enabled) {
		outputTabTimeStampingEnabled = enabled;
		getConfig().setProperty(OUTPUT_TAB_TIMESTAMPING_OPTION, enabled);
	}

	public boolean isOutputTabTimeStampingEnabled() {
		return outputTabTimeStampingEnabled;
	}
	
	public void setOutputTabTimeStampsFormat(String format) {
		outputTabTimeStampFormat = format;
		getConfig().setProperty(OUTPUT_TAB_TIMESTAMP_FORMAT, format);
	}

	public String getOutputTabTimeStampsFormat() {
		return outputTabTimeStampFormat;
	}

	/**
	 * Sets whether or not the HTTP CONNECT requests received by the local proxy should be (persisted and) shown in the UI.
	 *
	 * @param showConnectRequests {@code true} if the HTTP CONNECT requests should be shown, {@code false} otherwise
	 * @since 2.5.0
	 * @see #isShowLocalConnectRequests()
	 */
	public void setShowLocalConnectRequests(boolean showConnectRequests) {
		if (showLocalConnectRequests != showConnectRequests) {
			showLocalConnectRequests = showConnectRequests;
			getConfig().setProperty(SHOW_LOCAL_CONNECT_REQUESTS, showConnectRequests);
		}
	}

	/**
	 * Tells whether or not the HTTP CONNECT requests received by the local proxy should be (persisted and) shown in the UI.
	 * <p>
	 * The default is to not show the HTTP CONNECT requests.
	 *
	 * @return {@code true} if the HTTP CONNECT requests should be shown, {@code false} otherwise
	 * @since 2.5.0
	 * @see #setShowLocalConnectRequests(boolean)
	 */
	public boolean isShowLocalConnectRequests() {
		return showLocalConnectRequests;
	}

	public boolean isShowSplashScreen() {
		return showSplashScreen;
	}

	public void setShowSplashScreen(boolean showSplashScreen) {
		this.showSplashScreen = showSplashScreen;
		getConfig().setProperty(SPLASHSCREEN_OPTION, showSplashScreen);
	}

	public int getLargeRequestSize() {
		return largeRequestSize;
	}

	public void setLargeRequestSize(int largeRequestSize) {
		this.largeRequestSize = largeRequestSize;
	    LargeRequestUtil.setMinContentLength(largeRequestSize);
		getConfig().setProperty(LARGE_REQUEST_SIZE, largeRequestSize);
	}
	
	public int getLargeResponseSize() {
		return largeResponseSize;
	}

	public void setLargeResponseSize(int largeResponseSize) {
		this.largeResponseSize = largeResponseSize;
	    LargeResponseUtil.setMinContentLength(largeResponseSize);
		getConfig().setProperty(LARGE_RESPONSE_SIZE, largeResponseSize);
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		getConfig().setProperty(FONT_SIZE, fontSize);
	}

	public String getFontName() {
		return this.fontName;
	}
	
	public void setFontName(String fontName) {
		this.fontName = fontName;
		getConfig().setProperty(FONT_NAME, fontName);
	}
	
	public String getLookAndFeel() {
		return this.lookAndFeel;
	}
	
	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
		getConfig().setProperty(LOOK_AND_FEEL, lookAndFeel);
	}
	
	public boolean isScaleImages() {
		return scaleImages;
	}

	public void setScaleImages(boolean scaleImages) {
		this.scaleImages = scaleImages;
		getConfig().setProperty(SCALE_IMAGES, scaleImages);
	}

	public boolean isShowDevWarning() {
		return showDevWarning;
	}

	public void setShowDevWarning(boolean showDevWarning) {
		this.showDevWarning = showDevWarning;
		getConfig().setProperty(SHOW_DEV_WARNING, showDevWarning);
	}
	
    public boolean isConfirmRemoveProxyExcludeRegex() {
        return this.confirmRemoveProxyExcludeRegex;
    }

    public void setConfirmRemoveProxyExcludeRegex(boolean confirmRemove) {
        this.confirmRemoveProxyExcludeRegex = confirmRemove;
        getConfig().setProperty(CONFIRM_REMOVE_PROXY_EXCLUDE_REGEX_KEY, confirmRemove);
    }

    public boolean isConfirmRemoveScannerExcludeRegex() {
        return this.confirmRemoveScannerExcludeRegex;
    }

    public void setConfirmRemoveScannerExcludeRegex(boolean confirmRemove) {
        this.confirmRemoveScannerExcludeRegex = confirmRemove;
        getConfig().setProperty(CONFIRM_REMOVE_SCANNER_EXCLUDE_REGEX_KEY, confirmRemove);
    }

    public boolean isConfirmRemoveSpiderExcludeRegex() {
        return this.confirmRemoveSpiderExcludeRegex;
    }

    public void setConfirmRemoveSpiderExcludeRegex(boolean confirmRemove) {
        this.confirmRemoveSpiderExcludeRegex = confirmRemove;
        getConfig().setProperty(CONFIRM_REMOVE_SPIDER_EXCLUDE_REGEX_KEY, confirmRemove);
    }

    /**
     * Sets whether or not the system's locale should be used for formatting.
     *
     * @param useSystemsLocale {@code true} if the system's locale should be used for formatting, {@code false} otherwise.
     * @since 2.7.0
     * @see #isUseSystemsLocaleForFormat()
     * @see java.util.Locale.Category#FORMAT
     */
    public void setUseSystemsLocaleForFormat(boolean useSystemsLocale) {
        if (useSystemsLocaleForFormat != useSystemsLocale) {
            useSystemsLocaleForFormat = useSystemsLocale;
            getConfig().setProperty(USE_SYSTEMS_LOCALE_FOR_FORMAT_KEY, useSystemsLocaleForFormat);
        }
    }

    /**
     * Tells whether or not the system's locale should be used for formatting.
     *
     * @return {@code true} if the system's locale should be used for formatting, {@code false} otherwise.
     * @since 2.7.0
     * @see #setUseSystemsLocaleForFormat(boolean)
     * @see java.util.Locale.Category#FORMAT
     */
    public boolean isUseSystemsLocaleForFormat() {
        return useSystemsLocaleForFormat;
    }
}
