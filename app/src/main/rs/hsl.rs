#pragma version(1)
#pragma rs java_package_name(com.nullexcom.picture)

float *values;

static float findMin(float r, float g, float b) {
    return min(r, min(g, b));
}

static float findMax(float r, float g, float b) {
    return max(r, max(g, b));
}

static float3 toHSL(uchar4 in) {
    float r = in.r / 255.0;
    float g = in.g / 255.0;
    float b = in.b / 255.0;
    float cmin = findMin(r, g, b);
    float cmax = findMax(r, g, b);
    float c = cmax - cmin;
    float l = (cmax + cmin) / 2;
    float s = 0;
    if (c != 0) {
        s = c / (1 - fabs(2*l - 1));
    }
    float h = 0;
    if (c == 0) {
        h = 0;
    } else if (cmax == r) {
        h = 60 * ((g - b) / c);
    } else if (cmax == g) {
        h = 60 * (2 + (b - r) / c);
    } else if (cmax == b) {
        h = 60 * (4 + (r - g) / c);
    }
    float3 hsl = {h, s, l};
    return hsl;
}

static uchar4 toRGB(float h, float s, float l) {
    float c = (1 - fabs(2*l - 1)) * s;
    float cmax = (2*l + c) / 2.0;
    float cmin = (2*l - c) / 2.0;
    float r, g, b;
    if (h >= 0 && h < 60) {
        r = cmax;
        b = cmin;
        g = b + ((c * h) / 60.0);
    } else if (h >= 60 && h < 120) {
        g = cmax;
        b = cmin;
        r = b - (h / 60 - 2) * c;
    } else if (h >= 120 && h < 180) {
        r = cmin;
        g = cmax;
        b = r + (h / 60 - 2) * c;
    } else if (h >= 180 && h < 240) {
        r = cmin;
        b = cmax;
        g = r - (h / 60 - 4) * c;
    } else if (h >= 240 && h < 300) {
        g = cmin;
        b = cmax;
        r = g + (h / 60 - 4) * c;
    } else if (h >= 300 && h < 360) {
        h = h - 360;
        r = cmax;
        g = cmin;
        b = g - (h / 60) * c;
    }
    float3 debug = {r, g, b};
    uchar newR = (uchar) (r * 255);
    uchar newG = (uchar) (g * 255);
    uchar newB = (uchar) (b * 255);
    uchar4 rgb = {newR, newG, newB, 255};
    return rgb;
}

static float normal(float value, float v_min, float v_max) {
    if (value > v_max) {
        return v_max;
    } else if (value < v_min) {
        return v_min;
    } else {
        return value;
    }
}

uchar4 RS_KERNEL process(uchar4 in) {
    float3 hsl = toHSL(in);
    float h = hsl.x;
    float s = hsl.y;
    float l = hsl.z;
    if (h >= 0 && h < 30) {
        h = normal(h + values[0], 0, 30);
        s = normal(s + values[1], 0, 1);
        l = normal(l + values[2], 0, 1);
    } else if (h >= 30 && h < 60) {
        h = normal(h + values[3], 30, 60);
        s = normal(s + values[4], 0, 1);
        l = normal(l + values[5], 0, 1);
    } else if (h >= 60 && h < 90) {
        h = normal(h + values[6], 60, 90);
        s = normal(s + values[7], 0, 1);
        l = normal(l + values[8], 0, 1);
    } else if (h >= 90 && h < 120) {
        h = normal(h + values[9], 90, 120);
        s = normal(s + values[10], 0, 1);
        l = normal(l + values[11], 0, 1);
    } else if (h >= 120 && h < 150) {
        h = normal(h + values[12], 120, 150);
        s = normal(s + values[13], 0, 1);
        l = normal(l + values[14], 0, 1);
    } else if (h >= 150 && h < 180) {
        h = normal(h + values[15], 150, 180);
        s = normal(s + values[16], 0, 1);
        l = normal(l + values[17], 0, 1);
    } else if (h >= 180 && h < 210) {
        h = normal(h + values[18], 180, 210);
        s = normal(s + values[19], 0, 1);
        l = normal(l + values[20], 0, 1);
    } else if (h >= 210 && h < 240) {
        h = normal(h + values[21], 210, 240);
        s = normal(s + values[22], 0, 1);
        l = normal(l + values[23], 0, 1);
    } else if (h >= 240 && h < 270) {
        h = normal(h + values[24], 240, 270);
        s = normal(s + values[25], 0, 1);
        l = normal(l + values[26], 0, 1);
    } else if (h >= 270 && h < 300) {
        h = normal(h + values[27], 270, 300);
        s = normal(s + values[28], 0, 1);
        l = normal(l + values[29], 0, 1);
    } else if (h >= 300 && h < 330) {
        h = normal(h + values[30], 300, 330);
        s = normal(s + values[31], 0, 1);
        l = normal(l + values[32], 0, 1);
    } else {
        h = normal(h + values[33], 330, 360);
        s = normal(s + values[34], 0, 1);
        l = normal(l + values[35], 0, 1);
    }
    uchar4 rgba = toRGB(h, s, l);
    return rgba;
}