#ifndef __VISION_H__
#define __VISION_H__

#include "common.h"
#include "key.h"

#include <openssl/ssl.h>
#include <openssl/err.h>
#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <openssl/bio.h>
#include <openssl/buffer.h>

#define DESCRIPTION_SIZE 16
#define GOOGLE_VISION "172.217.24.238:443"
#define LABEL_NAME "0.jpg"

vector<float> getLabel(char *name, int size);
char *base64(char *input, int length);
char *useOCR(char *name);

#endif