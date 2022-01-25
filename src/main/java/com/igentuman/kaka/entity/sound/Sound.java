package com.igentuman.kaka.entity.sound;

import com.igentuman.kaka.setup.Registration;
import net.minecraft.sounds.SoundEvent;

import java.util.Random;

public class Sound {

    public static SoundEvent[] fart;

    public static SoundEvent[] getFartSounds()
    {
        if(fart == null) {
            fart = new SoundEvent[]{

            };
        }
        return fart;
    }

    public static SoundEvent getRandomFartSound()
    {
        Random r = new Random();
        int low = 0;
        int high = getFartSounds().length;
        return getFartSounds()[r.nextInt(high-low) + low];
    }
}
