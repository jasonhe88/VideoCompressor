package com.vincent.videocompressor;

import android.os.AsyncTask;

/**
 * Created by Vincent Woo
 * Date: 2017/8/16
 * Time: 15:15
 */

public class VideoCompress {
    private static final String TAG = VideoCompress.class.getSimpleName();

    public static VideoCompressTask compressVideoHigh(String srcPath, String destPath, CompressListener listener) {
        VideoCompressTask task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_HIGH);
        task.execute(srcPath, destPath);
        return task;
    }

    public static VideoCompressTask compressVideoMedium(String srcPath, String destPath, CompressListener listener) {
        VideoCompressTask task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_MEDIUM);
        task.execute(srcPath, destPath);
        return task;
    }

    public static VideoCompressTask compressVideoLow(String srcPath, String destPath, CompressListener listener) {
        VideoCompressTask task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_LOW);
        task.execute(srcPath, destPath);
        return task;
    }

    public static VideoCompressTask compressVideoCustom(String srcPath, String destPath, CompressConfig config, CompressListener listener) {
        VideoCompressTask task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_CUSTOM, config);
        task.execute(srcPath, destPath);
        return task;
    }

    public static void compressCancel() {
        VideoController.getInstance().cancelVideoConvert();
    }

    private static class VideoCompressTask extends AsyncTask<String, Float, Integer> {
        private CompressListener mListener;
        private int mQuality;
        private CompressConfig mConfig;

        public VideoCompressTask(CompressListener listener, int quality) {
            mListener = listener;
            mQuality = quality;
        }

        public VideoCompressTask(CompressListener listener, int quality, CompressConfig config) {
            mListener = listener;
            mQuality = quality;
            mConfig = config;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStart();
            }
        }

        @Override
        protected Integer doInBackground(String... paths) {
            return VideoController.getInstance().convertVideo(paths[0], paths[1], mQuality, mConfig, percent -> publishProgress(percent));
        }

        @Override
        protected void onProgressUpdate(Float... percent) {
            super.onProgressUpdate(percent);
            if (mListener != null) {
                mListener.onProgress(percent[0]);
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (mListener != null) {
                switch (result) {
                    case VideoController.COMPRESS_TYPE_CANCEL:
                        mListener.onCancel();
                        break;
                    case VideoController.COMPRESS_TYPE_SUCCESS:
                        mListener.onSuccess();
                        break;
                    case VideoController.COMPRESS_TYPE_FAIL:
                        mListener.onFail();
                        break;
                }
            }
        }
    }

    public interface CompressListener {
        void onStart();

        void onSuccess();

        void onCancel();

        void onFail();

        void onProgress(float percent);
    }
}
