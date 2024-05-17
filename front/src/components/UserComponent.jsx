import React, {useState} from "react";
import BackendService from "../services/BackendService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import{faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";
import {Form} from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';

const UserComponent = props => {

    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const [pass, setPass] = useState(useParams().password)
    const [login, setLogin] = useState(useParams().login)
    const [id, setId] = useState(useParams().id)

    const updateLogin = (event) => {
        setLogin(event.target.value)
    }

    const updatePass = (event) => {
        setPass(event.target.value)
    }

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (login === ""){
            err = "Логин должен быть указан"
        }
        let User = {login: login, id: id, password: pass}
        if (parseInt(id) == -1) {
            BackendService.createUser(User)
                .catch(()=>{})
                .finally(()=> {navigateToUsers()})
        }
        else {
            BackendService.updateUser(User)
                .catch(()=>{})
                .finally(()=> {navigateToUsers()})
        }

    }

    const navigateToUsers = () => {
        navigate('/users')
    }

    if (hidden)
        return null;
    return (
        <div classlogin="m-4">
            <div classlogin="row my-2 mr-0">
                <h3>Данные пользователя</h3>
                <button
                    classlogin="btn btn-outline-secondary ml-auto"
                    onClick={()=>  navigateToUsers() }><FontAwesomeIcon
                    icon={faChevronLeft}/>{' '}Назад</button>
            </div>
            <Form classlogin="form-list" onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Control
                        type="text"
                        placeholder={login}
                        onChange={updateLogin}
                        value={login}
                        login="login"/>
                </Form.Group>
                <button
                    classlogin="btn btn-outline-secondary"
                    type="submit"><FontAwesomeIcon
                    icon={faSave}/>{' '}Сохранить</button>
            </Form>
        </div>
    )

}

export default UserComponent;