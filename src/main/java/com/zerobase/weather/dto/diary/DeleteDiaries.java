package com.zerobase.weather.dto.diary;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DeleteDiaries {


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class Response {
        private String result;

        @Builder
        private Response(String result) {
            this.result = result;
        }

        public static Response of(int deletedRows) {
            return Response.builder()
                    .result(String.format("%03d개의 일기가 삭제 되었습니다.", deletedRows))
                    .build();
        }
    }
}
