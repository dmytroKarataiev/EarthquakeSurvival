/*
 * MIT License
 *
 * Copyright (c) 2016. Dmytro Karataiev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.adkdevelopment.earthquakesurvival.objects.earthquake;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by karataev on 3/24/16.
 */
public class Properties implements Parcelable {

    @SerializedName("mag")
    @Expose
    private Double mag;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("time")
    @Expose
    private Long time;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("tz")
    @Expose
    private Integer tz;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("felt")
    @Expose
    private String felt;
    @SerializedName("cdi")
    @Expose
    private String cdi;
    @SerializedName("mmi")
    @Expose
    private String mmi;
    @SerializedName("alert")
    @Expose
    private String alert;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tsunami")
    @Expose
    private Integer tsunami;
    @SerializedName("sig")
    @Expose
    private Integer sig;
    @SerializedName("net")
    @Expose
    private String net;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("ids")
    @Expose
    private String ids;
    @SerializedName("sources")
    @Expose
    private String sources;
    @SerializedName("types")
    @Expose
    private String types;
    @SerializedName("nst")
    @Expose
    private Integer nst;
    @SerializedName("dmin")
    @Expose
    private Double dmin;
    @SerializedName("rms")
    @Expose
    private Double rms;
    @SerializedName("gap")
    @Expose
    private Double gap;
    @SerializedName("magType")
    @Expose
    private String magType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;

    /**
     *
     * @return
     * The mag
     */
    public Double getMag() {
        return mag;
    }

    /**
     *
     * @param mag
     * The mag
     */
    public void setMag(Double mag) {
        this.mag = mag;
    }

    /**
     *
     * @return
     * The place
     */
    public String getPlace() {
        return place;
    }

    /**
     *
     * @param place
     * The place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     *
     * @return
     * The time
     */
    public Long getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     *
     * @return
     * The updated
     */
    public String getUpdated() {
        return updated;
    }

    /**
     *
     * @param updated
     * The updated
     */
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     *
     * @return
     * The tz
     */
    public Integer getTz() {
        return tz;
    }

    /**
     *
     * @param tz
     * The tz
     */
    public void setTz(Integer tz) {
        this.tz = tz;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     *
     * @param detail
     * The detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     *
     * @return
     * The felt
     */
    public Object getFelt() {
        return felt;
    }

    /**
     *
     * @param felt
     * The felt
     */
    public void setFelt(String felt) {
        this.felt = felt;
    }

    /**
     *
     * @return
     * The cdi
     */
    public Object getCdi() {
        return cdi;
    }

    /**
     *
     * @param cdi
     * The cdi
     */
    public void setCdi(String cdi) {
        this.cdi = cdi;
    }

    /**
     *
     * @return
     * The mmi
     */
    public Object getMmi() {
        return mmi;
    }

    /**
     *
     * @param mmi
     * The mmi
     */
    public void setMmi(String mmi) {
        this.mmi = mmi;
    }

    /**
     *
     * @return
     * The alert
     */
    public String getAlert() {
        return alert;
    }

    /**
     *
     * @param alert
     * The alert
     */
    public void setAlert(String alert) {
        this.alert = alert;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The tsunami
     */
    public Integer getTsunami() {
        return tsunami;
    }

    /**
     *
     * @param tsunami
     * The tsunami
     */
    public void setTsunami(Integer tsunami) {
        this.tsunami = tsunami;
    }

    /**
     *
     * @return
     * The sig
     */
    public Integer getSig() {
        return sig;
    }

    /**
     *
     * @param sig
     * The sig
     */
    public void setSig(Integer sig) {
        this.sig = sig;
    }

    /**
     *
     * @return
     * The net
     */
    public String getNet() {
        return net;
    }

    /**
     *
     * @param net
     * The net
     */
    public void setNet(String net) {
        this.net = net;
    }

    /**
     *
     * @return
     * The code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     * The ids
     */
    public String getIds() {
        return ids;
    }

    /**
     *
     * @param ids
     * The ids
     */
    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     *
     * @return
     * The sources
     */
    public String getSources() {
        return sources;
    }

    /**
     *
     * @param sources
     * The sources
     */
    public void setSources(String sources) {
        this.sources = sources;
    }

    /**
     *
     * @return
     * The types
     */
    public String getTypes() {
        return types;
    }

    /**
     *
     * @param types
     * The types
     */
    public void setTypes(String types) {
        this.types = types;
    }

    /**
     *
     * @return
     * The nst
     */
    public Integer getNst() {
        return nst;
    }

    /**
     *
     * @param nst
     * The nst
     */
    public void setNst(Integer nst) {
        this.nst = nst;
    }

    /**
     *
     * @return
     * The dmin
     */
    public Double getDmin() {
        return dmin;
    }

    /**
     *
     * @param dmin
     * The dmin
     */
    public void setDmin(Double dmin) {
        this.dmin = dmin;
    }

    /**
     *
     * @return
     * The rms
     */
    public Double getRms() {
        return rms;
    }

    /**
     *
     * @param rms
     * The rms
     */
    public void setRms(Double rms) {
        this.rms = rms;
    }

    /**
     *
     * @return
     * The gap
     */
    public Double getGap() {
        return gap;
    }

    /**
     *
     * @param gap
     * The gap
     */
    public void setGap(Double gap) {
        this.gap = gap;
    }

    /**
     *
     * @return
     * The magType
     */
    public String getMagType() {
        return magType;
    }

    /**
     *
     * @param magType
     * The magType
     */
    public void setMagType(String magType) {
        this.magType = magType;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mag);
        dest.writeString(this.place);
        dest.writeLong(this.time);
        dest.writeString(this.updated);
        dest.writeValue(this.tz);
        dest.writeString(this.url);
        dest.writeString(this.detail);
        dest.writeString(this.felt);
        dest.writeString(this.cdi);
        dest.writeString(this.mmi);
        dest.writeString(this.alert);
        dest.writeString(this.status);
        dest.writeValue(this.tsunami);
        dest.writeValue(this.sig);
        dest.writeString(this.net);
        dest.writeString(this.code);
        dest.writeString(this.ids);
        dest.writeString(this.sources);
        dest.writeString(this.types);
        dest.writeValue(this.nst);
        dest.writeValue(this.dmin);
        dest.writeValue(this.rms);
        dest.writeValue(this.gap);
        dest.writeString(this.magType);
        dest.writeString(this.type);
        dest.writeString(this.title);
    }

    public Properties() {
    }

    protected Properties(Parcel in) {
        this.mag = (Double) in.readValue(Double.class.getClassLoader());
        this.place = in.readString();
        this.time = in.readLong();
        this.updated = in.readString();
        this.tz = (Integer) in.readValue(Integer.class.getClassLoader());
        this.url = in.readString();
        this.detail = in.readString();
        this.felt = in.readString();
        this.cdi = in.readString();
        this.mmi = in.readString();
        this.alert = in.readString();
        this.status = in.readString();
        this.tsunami = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sig = (Integer) in.readValue(Integer.class.getClassLoader());
        this.net = in.readString();
        this.code = in.readString();
        this.ids = in.readString();
        this.sources = in.readString();
        this.types = in.readString();
        this.nst = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dmin = (Double) in.readValue(Double.class.getClassLoader());
        this.rms = (Double) in.readValue(Double.class.getClassLoader());
        this.gap = (Double) in.readValue(Double.class.getClassLoader());
        this.magType = in.readString();
        this.type = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<Properties> CREATOR = new Parcelable.Creator<Properties>() {
        @Override
        public Properties createFromParcel(Parcel source) {
            return new Properties(source);
        }

        @Override
        public Properties[] newArray(int size) {
            return new Properties[size];
        }
    };
}