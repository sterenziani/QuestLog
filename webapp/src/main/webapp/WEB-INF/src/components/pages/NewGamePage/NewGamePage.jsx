import React, { Component } from 'react';
import { 
    withTranslation 
} from 'react-i18next';
import { 
    Formik 
} from 'formik';
import * as Yup from 'yup';
import {
    Form
} from 'react-bootstrap';
import { 
    Helmet, 
    HelmetProvider 
} from "react-helmet-async";
import * as Parallel from 'paralleljs';

import AnyButton            from '../../common/AnyButton/AnyButton';

import CardForm             from '../../common/Forms/CardForm';
import FormikDatePicker     from '../../common/Forms/FormikDatePicker';
import FormikMultiSelect    from '../../common/Forms/FormikMultiSelect';

import withUser             from '../../hoc/withUser';

import GameService          from '../../../services/api/gameService';
import RegionService        from '../../../services/api/regionService';
import PlatformService      from '../../../services/api/platformService';
import DeveloperService     from '../../../services/api/devService';
import PublisherService     from '../../../services/api/publisherService';
import GenreService         from '../../../services/api/genreService';
import { CREATED, OK } from '../../../services/api/apiConstants';

const NewGameSchema = Yup.object().shape({
    title       : Yup
        .string()
        .required('createGame.fields.title.errors.is_required'),
    description : Yup
        .string()
        .max(15000, 'createGame.fields.description.errors.too_big')
})

class NewGamePage extends Component {
    state = {
        loading_game        : true,
        loading_releases    : true,
        loading_platforms   : true,
        loading_developers  : true,
        loading_publishers  : true,
        loading_genres      : true,
        initial_title         : undefined,
        initial_description   : undefined,
        initial_trailer       : undefined,
        initial_platforms     : undefined,
        initial_developers    : undefined,
        initial_publishers    : undefined,
        initial_genres        : undefined,
        initial_release_dates : undefined,
        releases        : [],
        platforms       : [],
        developers      : [],
        publishers      : [],
        genres          : [],
        cover           : undefined,
        cover_too_big   : false,
        cover_not_image : false,
        invalid_game    : false
    }

    componentDidMount = () => {
        this.load();
    }

    load            = async () => {
        if(this.props.editingMode){
            await this.loadGame();
            
        }
        this.setState({
            loading_game : false
        })
        this.fetchFromAPI();
    }

    loadGame        = async () => {
        const gameId   = this.props.match.params.id;
        const response = await GameService.getGameById(gameId);
        if(response.status){
            this.setState({
                invalid_game : true
            })
        } else {
            this.setState({
                initial_title       : response.title,
                initial_description : response.description,
                initial_trailer     : response.trailer,
            })

            const platforms_response        = await PlatformService.getGamePlatforms(gameId);
            if(!platforms_response.status){
                const platforms = platforms_response.map(p => ({ 'label' : p.name, 'value' : p.id }))
                this.setState({
                    initial_platforms : platforms
                })
            }

            const developers_response       = await DeveloperService.getGameDevelopers(gameId);
            if(!developers_response.status){
                const developers = developers_response.map(d => ({ 'label' : d.name, 'value' : d.id }))
                this.setState({
                    initial_developers : developers
                })
            }
            const publishers_response       = await PublisherService.getGamePublishers(gameId);
            if(!publishers_response.status){
                const publishers = publishers_response.map(p => ({ 'label' : p.name, 'value' : p.id }))
                this.setState({
                    initial_publishers : publishers
                })
            }
            const genres_response           = await GenreService.getGameGenres(gameId);
            if(!genres_response.status){
                const genres = genres_response.map(g => ({ 'label' : g.name, 'value' : g.id }))
                this.setState({
                    initial_genres : genres
                })
            }
            const releases_response         = await GameService.getGameReleaseDates(gameId);
            if(!releases_response.status){
                let releases = {};
                releases_response.forEach(r => {
                    releases[r.region.id] = r.date;
                })
                this.setState({
                    initial_release_dates : releases
                })
            }
        }
    }

    fetchFromAPI    = async () => {
        this.loadRegions()
        this.loadPlatforms()
        this.loadDevelopers()
        this.loadPublishers()
        this.loadGenres()
    }

    loadRegions     = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await RegionService.getEveryRegion()
            if(response.status){
                gotResponse = false;
            }
            if(!gotResponse){
                await new Promise(r => setTimeout(r, 5000));
            }
        } while (!gotResponse);

        response = new Parallel(response);

        const regions = response.map(r => ({ 'id' : r.id, 'region' : r.shortName }))
        this.setState({
            releases : regions.data,
            loading_releases : false
        })
    } 

    loadPlatforms   = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await PlatformService.getAllPlatforms()
            if(response.status){
                gotResponse = false;
            }
            if(!gotResponse){
                await new Promise(r => setTimeout(r, 5000));
            }
        } while (!gotResponse);

        response = new Parallel(response);

        const platforms = response.map(p => ({ 'label' : p.name, 'value' : p.id }))
        this.setState({
            platforms : platforms.data,
            loading_platforms : false
        })
    }

    loadDevelopers  = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await DeveloperService.getEveryDeveloper()
            if(response.status){
                gotResponse = false;
            }
            if(!gotResponse){
                await new Promise(r => setTimeout(r, 5000));
            }
        } while (!gotResponse);

        response = new Parallel(response);

        const developers = response.map(d => ({ 'label' : d.name, 'value' : d.id }))
        this.setState({
            developers : developers.data,
            loading_developers : false
        })
    }

    loadPublishers  = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await PublisherService.getEveryPublisher()
            if(response.status){
                gotResponse = false;
            }
            if(!gotResponse){
                await new Promise(r => setTimeout(r, 5000));
            }
        } while (!gotResponse);

        response = new Parallel(response);

        const publishers = response.map(p => ({ 'label' : p.name, 'value' : p.id }))
        this.setState({
            publishers : publishers.data,
            loading_publishers : false
        })
    }

    loadGenres      = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await GenreService.getAllGenres()
            if(response.status){
                gotResponse = false;
            }
            if(!gotResponse){
                await new Promise(r => setTimeout(r, 5000));
            }
        } while (!gotResponse);

        response = new Parallel(response);

        const genres = response.map(g => ({ 'label' : g.name, 'value' : g.id }))
        this.setState({
            genres : genres.data,
            loading_genres : false
        })
    }

    impactAPI = async (values, setFieldError) => {
        let releases = [];
        this.state.releases.forEach(r => {
            const releaseDate = values.region[r.id] == "" ? null : values.region[r.id]
            releases.push({'locale': r.id, 'date': releaseDate})
        });

        const toBase64 = file => new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result);
            reader.onerror = error => reject(error);
        });

        const cover         = values.cover.file ? await toBase64(values.cover.file) : null;
        const platforms     = values.platforms ? values.platforms.map(p => p.value) : [];
        const developers    = values.developers ? values.developers.map(d => d.value) : [];
        const publishers    = values.publishers ? values.publishers.map(p => p.value) : [];
        const genres        = values.genres ? values.genres.map(g => g.value) : [];

        let response;
        if(!this.props.editingMode){
            response = await GameService.register(values.title, values.description, cover, values.trailer, platforms, developers, publishers, genres, releases);
        } else {
            response = await GameService.editGame(this.props.match.params.id, values.title, values.description, cover, values.trailer, platforms, developers, publishers, genres, releases);
        }
        if(response.status != CREATED && response.status != OK){
            if(response.errors.includes('cover')){
                this.setState({
                    cover_not_image : true
                })
            }
            if(response.errors.includes('title')){
                setFieldError('title', 'createGame.fields.title.errors.conflict')
            }
        } else {
            this.props.addRedirection("game", `/games/${response.data.id}`)
            this.props.activateRedirect("game");
        }
    }

    onSubmit = (values, { setFieldError, setSubmitting }) => {
        setSubmitting(false);
        this.impactAPI(values, setFieldError);
    }

    onFileChanged = (event, setFieldValue) => {
        if(this.state.cover_not_image){
            this.setState({
                cover_not_image : false
            })
        }
        if(event.target.files && event.target.files[0]){
            if(event.target.files[0].size > 4000000){
                this.setState({
                    cover : undefined,
                    cover_too_big : true
                })
            } else {
                setFieldValue('cover.file', event.target.files[0])
                setFieldValue('cover.fileName', event.target.files[0].name)
                this.setState({
                    cover : event.target.files[0].name,
                    cover_too_big : false
                })
            }
        } else {
            this.setState({
                cover : undefined,
                cover_too_big : false
            })
        }
    }

    render() {
        const { t } = this.props
        if(this.state.loading_game){
            return <p></p>
        }
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{this.props.editingMode ? t('createGame.edit') : t('createGame.title')} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <Formik
                    initialValues = {{
                            title       : this.state.initial_title ? this.state.initial_title : '',
                            description : this.state.initial_description ? this.state.initial_description : '',
                            cover       : {
                                file     : '',
                                fileName : ''
                            },
                            trailer     : this.state.initial_trailer ? this.state.initial_trailer : '',
                            region      : this.state.initial_release_dates ? this.state.initial_release_dates : {},
                            platforms   : this.state.initial_platforms ? this.state.initial_platforms : [],
                            developers  : this.state.initial_developers ? this.state.initial_developers : [],
                            publishers  : this.state.initial_publishers ? this.state.initial_publishers : [],
                            genres      : this.state.initial_genres ? this.state.initial_genres : []
                        }
                    }
                    validationSchema={ NewGameSchema }
                    onSubmit = { this.onSubmit }
                >
                {({
                    values,
                    errors,
                    touched,
                    handleChange,
                    handleBlur,
                    handleSubmit,
                    isSubmitting,
                    setFieldValue,
                    setFieldTouched
                }) => (
                    
                    <CardForm
                        titleKey={this.props.editingMode ? "createGame.edit" : "createGame.title"}
                        onSubmit={ handleSubmit }
                    >
                        <Form.Group controlId="formGameTitle">
                            <Form.Label><strong>{t('createGame.fields.title.label')}</strong></Form.Label>
                            <Form.Control 
                                type="text" 
                                placeholder={t('createGame.fields.title.placeholder')}
                                name="title"
                                value={ values.title }
                                error={ errors.title }
                                touched={ touched.title }
                                onChange={ handleChange }
                                onBlur={ handleBlur } 
                            />
                            { 
                                <p className="form-error">
                                { errors.title && touched.title && errors.title && (
                                    t(errors.title)
                                )}
                                </p>
                            }
                        </Form.Group>
                        <Form.Group controlId="formGameDescription">
                            <Form.Label><strong>{t('createGame.fields.description.label')}</strong></Form.Label>
                            <Form.Control 
                                as="textarea" 
                                rows={8} 
                                placeholder={t('createGame.fields.description.placeholder')}
                                name="description"
                                value={ values.description }
                                error={ errors.description }
                                touched={ touched.description }
                                onChange={ handleChange }
                                onBlur={ handleBlur } 
                            />
                            { 
                                <p className="form-error">
                                { errors.description && touched.description && errors.description && (
                                    t(errors.description)
                                )}
                                </p>
                            }
                        </Form.Group>
                        <Form.Group>
                            <Form.Label><strong>{t('createGame.fields.cover.label')}</strong></Form.Label>
                            <Form.File 
                                id="cover"
                                name="cover"
                                label={ this.state.cover ? this.state.cover : t('createGame.fields.cover.placeholder') }
                                data-browse={t('createGame.fields.cover.browse')}
                                accept="image/*"
                                onChange={ e => this.onFileChanged(e, setFieldValue) }
                                onBlur={ handleBlur } 
                                custom
                            />
                            { 
                                <p className="form-error">
                                { this.state.cover_too_big && (
                                    t('createGame.fields.cover.errors.too_big')
                                )}
                                </p>
                            }
                            { 
                                <p className="form-error">
                                { this.state.cover_not_image && (
                                    t('createGame.fields.cover.errors.not_image')
                                )}
                                </p>
                            }
                        </Form.Group>
                        <Form.Group controlId="formGameTrailer">
                            <Form.Label><strong>{t('createGame.fields.trailer.label')}</strong></Form.Label>
                            <Form.Control 
                                type="text" 
                                placeholder={t('createGame.fields.trailer.placeholder')}
                                name="trailer"
                                value={ values.trailer }
                                error={ errors.trailer }
                                touched={ touched.trailer }
                                onChange={ handleChange }
                                onBlur={ handleBlur } 
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label><strong>{t('createGame.fields.preview.label')}</strong></Form.Label>
                            <div>
                                <iframe 
                                    id="preview" 
                                    width="286" 
                                    height="161" 
                                    title={`${t('createGame.fields.preview.label')}`}
                                    src={`https://www.youtube.com/embed/${values.trailer}`} 
                                    frameBorder="0" 
                                    allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" 
                                    allowFullScreen="">    
                                </iframe>
                            </div>
                        </Form.Group>
                        <Form.Group controlId="formGamePlatforms">
                            <Form.Label><strong>{t('createGame.fields.platforms.label')}</strong></Form.Label>
                            <FormikMultiSelect
                                id="platforms"
                                name="platforms"
                                placeholder={t('createGame.fields.platforms.placeholder')}
                                options={this.state.platforms}
                                value={values.platforms}
                                isMulti={true}
                                onChange={setFieldValue}
                                onBlur={setFieldTouched}
                                touched={touched.platforms}
                                error={errors.platforms}
                                isClearable={true}
                                backspaceRemovesValue={true}
                                isDisabled={this.state.loading_platforms}
                            />
                        </Form.Group>
                        <Form.Group controlId="formGameDevelopers">
                            <Form.Label><strong>{t('createGame.fields.developers.label')}</strong></Form.Label>
                            <FormikMultiSelect
                                id="developers"
                                name="developers"
                                placeholder={t('createGame.fields.developers.placeholder')}
                                options={this.state.developers}
                                value={values.developers}
                                isMulti={true}
                                onChange={setFieldValue}
                                onBlur={setFieldTouched}
                                touched={touched.developers}
                                error={errors.developers}
                                isClearable={true}
                                backspaceRemovesValue={true}
                                isDisabled={this.state.loading_developers}
                            />
                        </Form.Group>
                        <Form.Group controlId="formGamePublishers">
                            <Form.Label><strong>{t('createGame.fields.publishers.label')}</strong></Form.Label>
                            <FormikMultiSelect
                                id="publishers"
                                name="publishers"
                                placeholder={t('createGame.fields.publishers.placeholder')}
                                options={this.state.publishers}
                                value={values.publishers}
                                isMulti={true}
                                onChange={setFieldValue}
                                onBlur={setFieldTouched}
                                touched={touched.publishers}
                                error={errors.publishers}
                                isClearable={true}
                                backspaceRemovesValue={true}
                                isDisabled={this.state.loading_publishers}
                            />
                        </Form.Group>
                        <Form.Group controlId="formGameGenres">
                            <Form.Label><strong>{t('createGame.fields.genres.label')}</strong></Form.Label>
                            <FormikMultiSelect
                                id="genres"
                                name="genres"
                                placeholder={t('createGame.fields.genres.placeholder')}
                                options={this.state.genres}
                                value={values.genres}
                                isMulti={true}
                                onChange={setFieldValue}
                                onBlur={setFieldTouched}
                                touched={touched.genres}
                                error={errors.genres}
                                isClearable={true}
                                backspaceRemovesValue={true}
                                isDisabled={this.state.loading_genres}
                            />
                        </Form.Group>
                        <Form.Group controlId="formGameReleaseDate">
                            <Form.Label><strong>{t('createGame.fields.releaseDates.label')}</strong></Form.Label>
                            <div>
                            {
                                this.state.releases.map(release => (
                                    <Form.Group key={release.id} controlId={`formGameReleaseDate${release.id}`} className="d-flex align-items-stretch flex-column">
                                        <Form.Label>{release.region}</Form.Label>
                                        <div className="d-flex flex-column align-items-stretch">
                                            <FormikDatePicker
                                                dateFormat="yyyy-MM-dd"
                                                id={`region.${release.id}`}
                                                name={`region.${release.id}`}
                                                value={values.region[release.id]}
                                                onChange={setFieldValue}
                                                isDisabled={this.state.loading_releases}
                                                autocomplete="off"
                                            />
                                        </div>
                                        
                                    </Form.Group>
                                ))
                            }
                            </div>
                        </Form.Group>
                        <Form.Group>
                            <AnyButton 
                                variant="primary"
                                type="submit"
                                textKey={this.props.editingMode ? "createGame.saveChanges" : "createGame.add"}
                                disabled={ isSubmitting || this.state.loading_releases 
                                    || this.state.loading_platforms || this.state.loading_developers
                                    || this.state.loading_publishers || this.state.loading_genres }
                                block={true}
                            />
                        </Form.Group>
                    </CardForm>
                )} 
                </Formik>
            </React.Fragment>
        );
    }
}
 
export default withTranslation() (withUser(NewGamePage, { visibility: "adminOnly" }));