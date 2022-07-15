package voice;

import com.sedmelluq.discord.lavaplayer.container.MediaContainerDescriptor;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

public class LocalAudioTrackUnique extends LocalAudioTrack {
    /**
     * @param trackInfo             Track info
     * @param containerTrackFactory Probe track factory - contains the probe with its parameters.
     * @param sourceManager         Source manager used to load this track
     */
    public LocalAudioTrackUnique(AudioTrackInfo trackInfo, MediaContainerDescriptor containerTrackFactory, LocalAudioSourceManager sourceManager) {
        super(trackInfo, containerTrackFactory, sourceManager);
    }

    public static LocalAudioTrackUnique getOriginalInstance(LocalAudioTrack track) { // wont work sadly, TODO: investigate why JavaNoClassDefFoundException
        return new LocalAudioTrackUnique(track.getInfo(), track.getContainerTrackFactory(), (LocalAudioSourceManager) track.getSourceManager());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final LocalAudioTrack other = (LocalAudioTrack) obj;
        return (this.getIdentifier() == null) ? (other.getIdentifier() == null) : this.getIdentifier().equals(other.getIdentifier());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
