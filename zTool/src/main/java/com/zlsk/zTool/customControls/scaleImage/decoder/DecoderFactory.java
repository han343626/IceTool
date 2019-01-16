package com.zlsk.zTool.customControls.scaleImage.decoder;

public interface DecoderFactory<T> {
    /**
     * Produce a new instance of a decoder build type {@link T}.
     *
     * @return a new instance of your decoder.
     */
    T make() throws IllegalAccessException, InstantiationException;
}
