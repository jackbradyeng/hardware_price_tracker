DROP TABLE IF EXISTS "GPUs";

DROP TABLE IF EXISTS "GPUs";

CREATE TABLE "GPUs" (
    "model_number" varchar NOT NULL,
    "name" varchar,
    "chip_manufacturer" varchar,
    "board_manufacturer" varchar,
    "video_memory" smallint,
    "price" decimal,
    CONSTRAINT "gpu_key" PRIMARY KEY ("model_number")
);

DROP TABLE IF EXISTS "RAM";

CREATE TABLE "RAM" (
    "id" bigint NOT NULL,
    "name" varchar,
    "brand" varchar,
    "volume" smallint,
    "clock_rate" bigint,
    CONSTRAINT "ram_key" PRIMARY KEY ("id")
);