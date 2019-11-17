package com.vincent.videocompressor;

import android.media.MediaMetadataRetriever;
import android.util.Log;

/**
 * Created by XieZhiYing on 2019/7/26 10:40
 */
public class CompressConfig {

    int width = 0;//压缩宽度
    int height = 0;//压缩高度
    int bitrate = 0;//比特率
    int frame = 24;//帧
    int interval = 10;//关键帧
    int duration = 0; // 截取的时长，为零则不截取, 单位为毫秒

    public CompressConfig(int width, int height, int bitrate, int frame, int interval, int duration) {
        this.width = width;
        this.height = height;
        this.bitrate = bitrate;
        this.frame = frame;
        this.interval = interval;
        this.duration = duration;
    }

    public static class Builder {

        public int videoWidth = 0;//原视频宽度
        public int videoHeight = 0;//原视频高度
        public int videoBitrate = 0;//原视频比特率
        public int videoFrame = 0;//原视频帧
        public int videoRotation = 0;//原视频方向
        public long videoDuration = 0L;//原视频时长

        private int width = 0;//压缩宽度
        private int height = 0;//压缩高度
        private int bitrate = 0;//比特率
        private int frame = 0;//帧
        private int interval = 10;//关键帧
        private int duration = 0;

        /**
         * 读取视频信息，未做权限判断
         *
         * @param path
         */
        public Builder load(String path) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            videoWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            videoHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            videoRotation = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
            videoBitrate = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
            videoDuration = Long.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            videoFrame = Math.round(Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT)) * 1000 / videoDuration);
            this.width = videoWidth;
            this.height = videoWidth;
            this.frame = videoFrame;
            this.bitrate = videoBitrate;
            if (BuildConfig.DEBUG) {
                Log.e("xxx", toString());
            }
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder bitrate(int bitrate) {
            this.bitrate = bitrate;
            return this;
        }

        public Builder frame(int frame) {
            this.frame = frame;
            return this;
        }

        public Builder interval(int interval) {
            this.interval = interval;
            return this;
        }

        public Builder duration(int duration) {
            if(duration>0 && duration < videoDuration) this.duration = duration;
            return this;
        }


        public CompressConfig build() {
            return new CompressConfig(width, height, bitrate, frame, interval, duration);
        }

        @Override

        public String toString() {
            return "Builder{" +
                    "videoWidth=" + videoWidth +
                    ", videoHeight=" + videoHeight +
                    ", videoBitrate=" + videoBitrate +
                    ", videoFrame=" + videoFrame +
                    ", videoDuration=" + videoDuration +
                    ", videoRotation=" + videoRotation +
                    '}';
        }
    }
}
