package com.nullexcom.picture;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        float h = 320;
        float s = 0.6f;
        float l = 0.4f;
        int[] rgb = toRGB(h, s, l);
        assertEquals(163, rgb[0]);
        assertEquals(41, rgb[1]);
        assertEquals(122, rgb[2]);
    }

    private int[] toRGB(float h, float s, float l) {
        float c = (1 - Math.abs(2 * l - 1)) * s;
        float cmax = (2 * l + c) / 2;
        float cmin = (2 * l - c) / 2;
        float r = 0;
        float g = 0;
        float b = 0;
        if (h >= 0 && h < 60) {
            r = cmax;
            g = b + (c * h / 60);
            b = cmin;
        } else if (h >= 60 && h < 120) {
            r = b - (h / 60 - 2) * c;
            g = cmax;
            b = cmin;
        } else if (h >= 120 && h < 180) {
            r = cmin;
            g = cmax;
            b = r + (h / 60 - 2) * c;
        } else if (h >= 180 && h < 240) {
            r = cmin;
            g = r - (h / 60 - 4) * c;
            b = cmax;
        } else if (h >= 240 && h < 300) {
            r = g + (h / 60 - 4) * c;
            g = cmin;
            b = cmax;
        } else if (h >= 300 && h < 360) {
            h = h - 360;
            r = cmax;
            g = cmin;
            b = g - (h / 60) * c;
        }
        return new int[]{
                Math.round(r * 255), Math.round(g * 255), Math.round(b * 255), 255
        };
    }
}