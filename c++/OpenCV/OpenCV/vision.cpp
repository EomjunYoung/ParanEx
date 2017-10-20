#include "vision.h"
#include <locale>

vector<float> getLabel(char *name, int size) {
	vector<float> labels;
	float start;
	bool manymarks = false;
	for (char *p = strstr(useOCR(name), "\"description\":") + DESCRIPTION_SIZE; *p != '\"'; p++) {	//읽은 문자열 인식 - 8부터 시작 9부터 시작?
		if (*p <= '9' && *p > '0') {
			start = *p - '0';
			labels.push_back(start);
			if (strstr(p, "30"))
				manymarks = true;
			break;
		}
	}
	if (labels.size() == 0) {
		printf("error), cannot read number label!\n");
		waitKey();
		exit(1);
	}

	float time = start + ((size > 15 || manymarks) ? 0.5f : 1);
	for (int i = 1; i < size; i++) {
		if (size > 15 || manymarks) {
			labels.push_back(time);
			time += 0.5f;
		}
		else {
			labels.push_back(time);
			time += 1;
		}
	}

	return labels;
}

/* reference : https://devenix.wordpress.com/2008/01/18/howto-base64-encode-and-decode-with-c-and-openssl-2/ */
char *base64(char *input, int length)
{
	BIO *bmem, *b64;
	BUF_MEM *bptr;

	b64 = BIO_new(BIO_f_base64());
	bmem = BIO_new(BIO_s_mem());
	b64 = BIO_push(b64, bmem);
	BIO_write(b64, input, length);
	BIO_flush(b64);
	BIO_get_mem_ptr(b64, &bptr);

	char *buff = (char *)malloc(bptr->length);
	memcpy(buff, bptr->data, bptr->length - 1);
	buff[bptr->length - 1] = 0;

	BIO_free_all(b64);

	return buff;
}
/* reference : https://devenix.wordpress.com/2008/01/18/howto-base64-encode-and-decode-with-c-and-openssl-2/ */

void useOCR() {

	BIO* bio;
	SSL* ssl;
	SSL_CTX* ctx;
	FILE *fp = NULL;
	char post[] = "POST /v1/images:annotate?key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX HTTP/1.1\r\n";
	char *pc = strstr(post, "XX");
	for (int i = 0, length = strlen(VISION_KEY); i < length; *(pc + i) = VISION_KEY[i], i++);
	char host[] = "Host: vision.googleapis.com\r\n";
	char *buffer;

	SSL_library_init();

	char name[][6] = { "1.jpg","2.jpg","3.jpg","4.jpg","5.jpg" };
	for (int i = 0; i < 5; i++) {

		fp = fopen(name[i], "rb");
		if (fp == NULL) {
			printf("error), cannot open file!\n");
			waitKey();
			exit(1);
		}

		ctx = SSL_CTX_new(SSLv23_client_method());
		if (ctx == NULL) {
			printf("error), ctx is null!\n");
			waitKey();
			exit(1);
		}

		bio = BIO_new_ssl_connect(ctx);
		BIO_set_conn_hostname(bio, GOOGLE_VISION);

		if (BIO_do_connect(bio) <= 0) {
			printf("error), cannot connect to Google!\n");
			waitKey();
			exit(1);
		}

		fseek(fp, 0, SEEK_END);
		int fileSize = ftell(fp);
		fseek(fp, 0, SEEK_SET);

		buffer = (char*)malloc(fileSize + 1);

		fread(buffer, fileSize, 1, fp);
		char *content = base64(buffer, fileSize);
		free(buffer);
		char *request = (char*)malloc((strlen(content) + 140 + 1) * sizeof(char));
		strcpy(request, "{\"requests\":[{\"image\":{\"content\":\"");
		strcat(request, content);

		strcat(request, "\"},\"features\" : {\"type\":\"TEXT_DETECTION\",\"maxResults\" : 10}}]}");
		int digitSize = 0;
		for (int i = strlen(request); i > 0; digitSize++, i /= 10);
		char *length = (char*)malloc((20 + digitSize + 1) * sizeof(char));
		sprintf(length, "Content-Length: %d\r\n\r\n", strlen(request));

		char *message = (char*)malloc((strlen(post) + strlen(host) + strlen(length) + strlen(request) + 1) * sizeof(char));
		strcpy(message, post);
		strcat(message, host);
		strcat(message, length);
		strcat(message, request);

		if (BIO_write(bio, message, strlen(message)) <= 0) {
			printf("error), fail to send message!\n");
			waitKey();
			exit(1);
		}

		buffer = (char*)malloc(BUFF_SIZE * 2 * sizeof(char));
		BIO_read(bio, buffer, BUFF_SIZE * 2);
		printf("%d\n\n", i + 1);
		setlocale(LC_ALL, "ko_KR.UTF-8");
		if(strstr(buffer,"\"description\"")!=NULL)
			printf("%s\n",buffer);

		BIO_free_all(bio);
		SSL_CTX_free(ctx);

	}
}

char *useOCR(char *name) {

	BIO* bio;
	SSL* ssl;
	SSL_CTX* ctx;
	FILE *fp = NULL;
	char post[] = "POST /v1/images:annotate?key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX HTTP/1.1\r\n";
	char *pc = strstr(post, "XX");
	for (int i = 0, length = strlen(VISION_KEY); i < length; *(pc + i) = VISION_KEY[i], i++);
	char host[] = "Host: vision.googleapis.com\r\n";
	char *buffer;
	SSL_library_init();
	ctx = SSL_CTX_new(SSLv23_client_method());

	if (ctx == NULL) {
		printf("error), ctx is null!\n");
		waitKey();
		exit(1);
	}

	bio = BIO_new_ssl_connect(ctx);
	BIO_set_conn_hostname(bio, GOOGLE_VISION);

	if (BIO_do_connect(bio) <= 0) {
		printf("error), cannot connect to Google!\n");
		waitKey();
		exit(1);
	}

	fp = fopen(name, "rb");
	if (fp == NULL) {
		printf("error), cannot open file!\n");
		waitKey();
		exit(1);
	}

	fseek(fp, 0, SEEK_END);
	int fileSize = ftell(fp);
	fseek(fp, 0, SEEK_SET);

	buffer = (char*)malloc(fileSize + 1);

	fread(buffer, fileSize, 1, fp);
	char *content = base64(buffer, fileSize);
	free(buffer);
	char *request = (char*)malloc((strlen(content) + 140 + 1) * sizeof(char));
	strcpy(request, "{\"requests\":[{\"image\":{\"content\":\"");
	strcat(request, content);

	strcat(request, "\"},\"features\" : {\"type\":\"TEXT_DETECTION\",\"maxResults\" : 10}}]}");
	int digitSize = 0;
	for (int i = strlen(request); i > 0; digitSize++, i /= 10);
	char *length = (char*)malloc((20 + digitSize + 1) * sizeof(char));
	sprintf(length, "Content-Length: %d\r\n\r\n", strlen(request));

	char *message = (char*)malloc((strlen(post) + strlen(host) + strlen(length) + strlen(request) + 1) * sizeof(char));
	strcpy(message, post);
	strcat(message, host);
	strcat(message, length);
	strcat(message, request);

	if (BIO_write(bio, message, strlen(message)) <= 0) {
		printf("error), fail to send message!\n");
		waitKey();
		exit(1);
	}

	buffer = (char*)malloc(BUFF_SIZE * 2 * sizeof(char));
	BIO_read(bio, buffer, BUFF_SIZE * 2);

	BIO_free_all(bio);
	SSL_CTX_free(ctx);

	return buffer;
}