import React, { FC, ReactElement } from "react";
import { useDispatch, useSelector } from "react-redux";
import { LoginOutlined, LogoutOutlined, ShoppingCartOutlined, UserAddOutlined, UserOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";
import { Affix, Badge, Col, Row, Space } from "antd";

import { selectUserFromUserState } from "../../redux-toolkit/user/user-selector";
import { selectCartItemsCount } from "../../redux-toolkit/cart/cart-selector";
import { logoutSuccess } from "../../redux-toolkit/user/user-slice";
import { ACCOUNT, BASE, CONTACTS, LOGIN, MENU, REGISTRATION } from "../../constants/routeConstants";
import { CART } from "../../constants/urlConstants";
import "./NavBar.scss";

const NavBar: FC = (): ReactElement => {
    const dispatch = useDispatch();
    const usersData = useSelector(selectUserFromUserState);
    const cartItemsCount = useSelector(selectCartItemsCount);

    const handleLogout = (): void => {
        localStorage.removeItem("token");
        dispatch(logoutSuccess());
    };

    return (
        <>
            <Affix>
                <div className={"navbar-wrapper"}>
                    <Row style={{ padding: "0px 400px" }}>
                        <Col span={24}>
                            <ul className="navbar-menu">
                                <li>
                                    <Link to={BASE}>HOME</Link>
                                </li>
                                <li>
                                    <Link to={{ pathname: MENU, state: { id: "all" } }}>PERFUMES</Link>
                                </li>
                                <li>
                                    <Link to={CONTACTS}>CONTACTS</Link>
                                </li>
                                <li className={"navbar-cart"}>
                                    <Badge count={cartItemsCount} size="small" color={"green"}>
                                        <Link to={CART}>
                                            <ShoppingCartOutlined />
                                        </Link>
                                    </Badge>
                                </li>
                                {usersData ? (
                                    <>
                                        <li>
                                            <Link to={ACCOUNT}>
                                                <Space align={"baseline"}>
                                                    <UserOutlined />
                                                    MY ACCOUNT
                                                </Space>
                                            </Link>
                                        </li>
                                        <li>
                                            <Link id={"handleLogout"} to={BASE} onClick={handleLogout}>
                                                <Space align={"baseline"}>
                                                    <LogoutOutlined />
                                                    EXIT
                                                </Space>
                                            </Link>
                                        </li>
                                    </>
                                ) : (
                                    <>
                                        <li>
                                            <Link to={LOGIN}>
                                                <Space align={"baseline"}>
                                                    <LoginOutlined />
                                                    SIGN IN
                                                </Space>
                                            </Link>
                                        </li>
                                        <li>
                                            <Link to={REGISTRATION}>
                                                <Space align={"baseline"}>
                                                    <UserAddOutlined />
                                                    SIGN UP
                                                </Space>
                                            </Link>
                                        </li>
                                    </>
                                )}
                            </ul>
                        </Col>
                    </Row>
                </div>
            </Affix>
        </>
    );
};

export default NavBar;
