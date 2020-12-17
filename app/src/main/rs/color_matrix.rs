#pragma version(1)
#pragma rs java_package_name(com.nullexcom.picture)

float4 v1;
float4 v2;
float4 v3;
float4 v4;

uchar4 RS_KERNEL process(uchar4 in, uint32_t x, uint32_t y) {
  uchar r = in.r;
  uchar g = in.g;
  uchar b = in.b;
  uchar newR = v1[0]*r + v2[0]*g + v3[0]*b + v4[0] * 3;
  uchar newG = v1[1]*r + v2[1]*g + v3[1]*b + v4[1] * 3;
  uchar newB = v1[2]*r + v2[2]*g + v3[2]*b + v4[2] * 3;
  if (newR > 255) newR = 255;
  else if (newR < 0) newR = 0;
  if (newG > 255) newG = 255;
  else if (newG < 0) newG = 0;
  if (newB > 255) newB = 255;
  else if (newB < 0) newB = 0;
  uchar4 out = {newR, newG, newB, 255};
  float3 debug = {v1[3], v2[3], v3[3]};
  return out;
}