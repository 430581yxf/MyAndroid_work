package com.example.myapplicationui.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShortVideo {
        private int id;
        private String path;
        private String title;
        private String description;
        private int numLikes;
        private int numComments;
        private int numStores;
        private boolean isLiked;
        private boolean isStored;
        private String imageUrl;
        private String makerName;
        private int makerId;
        private String category;
}
