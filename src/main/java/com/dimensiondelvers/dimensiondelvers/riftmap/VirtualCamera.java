package com.dimensiondelvers.dimensiondelvers.riftmap;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class VirtualCamera {
    private Vector3f position;
    private float pitch, yaw, roll;
    private float fov, aspectRatio, nearPlane, farPlane;

    public VirtualCamera(float fov, float aspectRatio, float nearPlane, float farPlane) {
        this.position = new Vector3f(0, 10, 0);
        this.pitch = 80;
        this.yaw = 0;
        this.roll = 0;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public void setRotation(float pitch, float yaw, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public Matrix4f getViewMatrix() {
        Matrix4f view = new Matrix4f();
        view.identity();
        view.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(roll), new Vector3f(0, 0, 1));
        view.translate(-position.x, -position.y, -position.z);
        return view;
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective((float) Math.toRadians(fov), aspectRatio, nearPlane, farPlane);
    }
}
