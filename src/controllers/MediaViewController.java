package controllers;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.io.File;


public class MediaViewController extends Controller{

    @FXML private Pane _anchor;
    @FXML private HBox _hBox;
    @FXML private Slider _slider;
    @FXML private Button _play;
    @FXML private Button _skipforward;
    @FXML private Button _faster;
    @FXML private Button _slower;
    @FXML private Label _timelabel;
    @FXML private Button _skipbackward;
    private String _previousfxmlpath =  "/fxml/ListScene.fxml";
    private File _fileUrl;
    private Media _video;
    private MediaPlayer _player;
    private MediaView _view = new MediaView();
    private double _rate = 1.0;

    @FXML
    public void Initialize(){

        _player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                String time = "";
                time+=String.format("%02d", ((int)newValue.toMinutes()));;
                time+=":";
                time+=String.format("%02d", ((int)newValue.toSeconds()) % 60);
                _timelabel.setText(time);

            }

        });


        _player.setOnEndOfMedia(()-> {

            _play.setDisable(true);
            _slider.adjustValue(_player.getTotalDuration().toMillis());

            String time = "";
            time+=String.format("%02d", ((int)_player.getTotalDuration().toMinutes()));
            time+=":";
            time+=String.format("%02d", ((int)_player.getTotalDuration().toSeconds())%60);
            _timelabel.setText(time);


            _slower.setDisable(true);
            _faster.setDisable(true);
            _skipbackward.setDisable(true);
            _skipforward.setDisable(true);

        });


        _player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                double time = newValue.toMillis();
                _slider.adjustValue(time);

            }

        });

    }


    public void playMedia(String medianame) {

        _fileUrl = new File("./creations/" + medianame + ".mp4");
        _video = new Media(_fileUrl.toURI().toString());
        _player = new MediaPlayer(_video);
        _player.setAutoPlay(true);
        _view.setMediaPlayer(_player);
        _view.setFitWidth(1000);
        _view.setFitHeight(416);
        _hBox.getChildren().addAll(_view);


        _player.setOnReady(new Runnable() {

            @Override
            public void run() {

                Initialize();
                _slider.setMin(0);
                _slider.setMax(_player.getTotalDuration().toMillis());

            }

        });

    }



    public void PausePlay(){

            if (_player.getStatus() == MediaPlayer.Status.PLAYING) {

                _player.pause();
                _play.setText("Play");

            } else {

                _player.play();
                _play.setText("Pause");

            }
    }




    public void FastForward() {

        if ((_rate + 0.25) <= 2) {

            _rate = _rate + 0.25;
            _player.setRate(_rate);

        }

    }

    public void SlowDown() {

        if ((_rate - 0.25) > 0) {
            _rate = _rate - 0.25;
            _player.setRate(_rate);
        }

    }

    public void SkipForwardSeconds() {

        _player.seek(_player.getCurrentTime().add(Duration.seconds(5)));
    }

    public void SkipBackSeconds() {

        _player.seek(_player.getCurrentTime().add(Duration.seconds(-5)));
    }





    @Override
    public String ReturnFXMLPath() {

        return _previousfxmlpath;

    }

    @Override
    public String ReturnForwardFXMLPath() {

        return null;

    }

    @Override
    public void AuxiliaryFunctionBackwards(FXMLLoader loader) {

        _player.stop();
        ListScene listcontroller = loader.<ListScene>getController();
        listcontroller.innitialiseList();

    }

}
