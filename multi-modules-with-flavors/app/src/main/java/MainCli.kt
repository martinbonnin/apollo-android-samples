import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroup
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.ExoTrackSelection
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.common.collect.ImmutableList
import com.library.GetUserQuery

fun main() {
  println("Compilation of ${GetUserQuery} was successful")

}
class CustomAdaptiveTrackSelection(
        group: TrackGroup,
        tracks: IntArray,
        bandwidthMeter: BandwidthMeter,
        private val someParameter: Int
) : AdaptiveTrackSelection(
        group,
        tracks,
        bandwidthMeter
) {
  class Factory(
          private val someParameter: Int
  ) : AdaptiveTrackSelection.Factory() {
    @JvmSynthetic
    override fun createAdaptiveTrackSelection(
            group: TrackGroup,
            tracks: IntArray,
            type: Int,
            bandwidthMeter: BandwidthMeter,
            adaptationCheckpoints: ImmutableList<AdaptationCheckpoint> // compilation error
    ): AdaptiveTrackSelection {
      return CustomAdaptiveTrackSelection(
              group,
              tracks,
              bandwidthMeter,
              someParameter
      )
    }
  }
}