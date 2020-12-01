package com.cxp.im.bean;

import java.util.List;

/**
 * 文 件 名: TestBean
 * 创 建 人: CXP
 * 创建日期: 2020-10-21 17:38
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class TestBean {

    private String name;
    private List<UserBean> list;

    public TestBean(String name, List<UserBean> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class UserBean {
        private String user;
        private List<BookBean> list;

        public UserBean(String user, List<BookBean> list) {
            this.user = user;
            this.list = list;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public static class BookBean {
            private String bookName;
            private List<CarBean> list;

            public BookBean(String bookName, List<CarBean> list) {
                this.bookName = bookName;
                this.list = list;
            }

            public String getBookName() {
                return bookName;
            }

            public void setBookName(String bookName) {
                this.bookName = bookName;
            }

            public static class CarBean {
                private String carName;

                public CarBean(String carName) {
                    this.carName = carName;
                }

                public String getCarName() {
                    return carName;
                }

                public void setCarName(String carName) {
                    this.carName = carName;
                }
            }
        }

    }
}
